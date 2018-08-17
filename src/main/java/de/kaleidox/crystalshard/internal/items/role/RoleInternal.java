package de.kaleidox.crystalshard.internal.items.role;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.items.permission.PermissionSetInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.items.permission.PermissionSet;
import de.kaleidox.crystalshard.main.items.role.Role;
import de.kaleidox.crystalshard.main.items.server.Server;

import java.awt.*;
import java.util.concurrent.CompletableFuture;

public class RoleInternal implements Role {
    private final Server server;
    private final PermissionSet permissions;
    private final long id;
    private final String name;
    private final Color color;
    private final boolean grouping;
    private final int position;
    private final boolean managed;
    private final boolean mentionable;
    private final Discord discordInternal;

    public RoleInternal(Discord discord, Server server, JsonNode data) {
        this.discordInternal = discord;
        this.server = server;
        this.id = data.get("id").asLong();
        this.name = data.get("name").asText();
        this.color = new Color(data.get("color").asInt());
        this.grouping = data.get("hoist").asBoolean();
        this.position = data.get("position").asInt();
        this.permissions = new PermissionSetInternal(data.get("permissions").asInt());
        this.managed = data.get("manages").asBoolean();
        this.mentionable = data.get("mentionable").asBoolean();
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
    public PermissionSet getPermissions() {
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
        return "<@&"+id+">";
    }

    @Override
    public String getName() {
        return name;
    }
}
