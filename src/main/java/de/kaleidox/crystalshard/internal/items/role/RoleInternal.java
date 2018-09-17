package de.kaleidox.crystalshard.internal.items.role;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.core.net.request.Endpoint;
import de.kaleidox.crystalshard.core.net.request.Method;
import de.kaleidox.crystalshard.core.net.request.WebRequest;
import de.kaleidox.crystalshard.internal.items.permission.PermissionListInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.handling.editevent.EditTrait;
import de.kaleidox.crystalshard.main.handling.listener.ListenerManager;
import de.kaleidox.crystalshard.main.handling.listener.server.role.RoleAttachableListener;
import de.kaleidox.crystalshard.main.items.permission.PermissionList;
import de.kaleidox.crystalshard.main.items.role.Role;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.logging.Logger;
import de.kaleidox.util.objects.Evaluation;

import java.awt.Color;
import java.util.Collection;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import static de.kaleidox.crystalshard.main.handling.editevent.enums.RoleEditTrait.*;

public class RoleInternal implements Role {
    private final static Logger                        logger    = new Logger(RoleInternal.class);
    private final static ConcurrentHashMap<Long, Role> instances = new ConcurrentHashMap<>();
    private final        Server                        server;
    private final        long                          id;
    private final        Discord                       discordInternal;
    private              PermissionList                permissions;
    private              String                        name;
    private              Color                         color;
    private              boolean                       grouping;
    private              int                           position;
    private              boolean                       managed;
    private              boolean                       mentionable;
    
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
    
// Override Methods
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
    
    @Override
    public <C extends RoleAttachableListener> ListenerManager<C> attachListener(C listener) {
        return null; // todo
    }
    
    @Override
    public Evaluation<Boolean> detachListener(RoleAttachableListener listener) {
        return null;
    }
    
    @Override
    public Collection<RoleAttachableListener> getAttachedListeners() {
        return null;
    }
    
    public Set<EditTrait<Role>> updateData(JsonNode data) {
        HashSet<EditTrait<Role>> traits = new HashSet<>();
        
        if (permissions.toPermissionInt() != data.path("permissions").asInt(permissions.toPermissionInt())) {
            permissions = new PermissionListInternal(data.get("permissions").asInt());
            traits.add(PERMISSION_OVERWRITES);
        }
        if (!name.equals(data.path("name").asText(name))) {
            name = data.get("name").asText();
            traits.add(NAME);
        }
        if (!color.equals(new Color(data.path("color").asInt(color.getRGB())))) {
            color = new Color(data.get("color").asInt());
            traits.add(COLOR);
        }
        if (grouping != data.path("hoist").asBoolean(grouping)) {
            grouping = data.get("hoist").asBoolean();
            traits.add(GROUPING);
        }
        if (position != data.path("position").asInt(position)) {
            position = data.get("position").asInt();
            traits.add(POSITION);
        }
        if (managed != data.path("managed").asBoolean(managed)) {
            managed = data.get("managed").asBoolean();
            traits.add(MANAGED);
        }
        if (mentionable != data.path("mentionable").asBoolean(mentionable)) {
            mentionable = data.get("mentionable").asBoolean();
            traits.add(MENTIONABILITY);
        }
        
        return traits;
    }
    
// Static membe
    public static Role getInstance(Server server, JsonNode data) {
        long id = data.path("id").asLong(-1);
        assert id != -1 : "No valid ID found.";
        return instances.containsKey(id) ? instances.get(id) : new RoleInternal(server.getDiscord(), server, data);
    }
    
    public static Role getInstance(Server server, long id) {
        assert id != -1 : "No valid ID found.";
        return instances.getOrDefault(id,
                                      new WebRequest<Role>(server.getDiscord()).endpoint(Endpoint.Location.ROLE.toEndpoint(
                                              server)).method(Method.GET).execute(node -> {
                                          for (JsonNode role : node) {
                                              if (role.get("id").asLong() == id)
                                                  return new RoleInternal(server.getDiscord(), server, role);
                                          }
                                          throw new NoSuchElementException("No Role with ID [" + id + "] found.");
                                      }).join());
    }
}
