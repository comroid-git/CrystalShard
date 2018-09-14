package de.kaleidox.crystalshard.internal.items.role;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.core.net.request.Endpoint;
import de.kaleidox.crystalshard.core.net.request.Method;
import de.kaleidox.crystalshard.core.net.request.WebRequest;
import de.kaleidox.crystalshard.internal.items.permission.PermissionListInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.items.permission.PermissionList;
import de.kaleidox.crystalshard.main.items.role.Role;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.logging.Logger;

import java.awt.*;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class RoleInternal implements Role {
    private final static Logger logger = new Logger(RoleInternal.class);
    private final static ConcurrentHashMap<Long, Role> instances = new ConcurrentHashMap<>();
    private final Server server;
    private final PermissionList permissions;
    private final long id;
    private final String name;
    private final Color color;
    private final boolean grouping;
    private final int position;
    private final boolean managed;
    private final boolean mentionable;
    private final Discord discordInternal;

    private RoleInternal(Discord discord, Server server, JsonNode data) {
        logger.deeptrace("Creating role object for data: " + data.toString());
        this.discordInternal = discord;
        this.server = server;
        this.id = data.get("id").asLong();
        this.name = data.get("name").asText();
        this.color = new Color(data.get("color").asInt());
        this.grouping = data.get("hoist").asBoolean();
        this.position = data.get("position").asInt();
        this.permissions = new PermissionListInternal(data.get("permissions").asInt());
        this.managed = data.get("managed").asBoolean();
        this.mentionable = data.get("mentionable").asBoolean();

        instances.putIfAbsent(id, this);
    }

    public static Role getInstance(Server server, JsonNode data) {
        long id = data.path("id").asLong(-1);
        assert id != -1 : "No valid ID found.";
        return instances.containsKey(id) ? instances.get(id) : new RoleInternal(server.getDiscord(), server, data);
    }

    public static Role getInstance(Server server, long id) {
        assert id != -1 : "No valid ID found.";
        return instances.getOrDefault(id,
                new WebRequest<Role>(server.getDiscord())
                        .endpoint(Endpoint.Location.ROLE.toEndpoint(server))
                        .method(Method.GET)
                        .execute(node -> {
                            for (JsonNode role : node) {
                                if (role.get("id").asLong() == id)
                                    return new RoleInternal(server.getDiscord(), server, role);
                            }
                            throw new NoSuchElementException("No Role with ID [" + id + "] found.");
                        })
                        .join()
        );
    }

    @Override
    public CompletableFuture<Server> getServer() {
        if (server != null) {
            return CompletableFuture.completedFuture(server);
        } else {
            return null; // todo
        }
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public boolean isGrouping() {
        return grouping;
    }

    @Override
    public boolean isManaged() {
        return managed;
    }

    @Override
    public boolean isMentionable() {
        return mentionable;
    }

    @Override
    public int getPosition() {
        return position;
    }

    @Override
    public PermissionList getPermissions() {
        return permissions;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public Discord getDiscord() {
        return discordInternal;
    }

    /**
     * Gets the theoretical mention tag for the role.
     *
     * @return The mention tag.
     * @see Role#isMentionable()
     */
    @Override
    public String getMentionTag() {
        return "<@&" + id + ">";
    }

    @Override
    public String getName() {
        return name;
    }
}
