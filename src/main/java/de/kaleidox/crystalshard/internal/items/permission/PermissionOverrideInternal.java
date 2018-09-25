package de.kaleidox.crystalshard.internal.items.permission;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.items.permission.OverrideState;
import de.kaleidox.crystalshard.main.items.permission.Permission;
import de.kaleidox.crystalshard.main.items.permission.PermissionList;
import de.kaleidox.crystalshard.main.items.permission.PermissionOverride;
import de.kaleidox.crystalshard.main.items.permission.PermissionOverwritable;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.util.helpers.JsonHelper;
import de.kaleidox.util.objects.markers.IDPair;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static de.kaleidox.crystalshard.main.items.permission.OverrideState.*;

public class PermissionOverrideInternal extends ConcurrentHashMap<Permission, OverrideState>
        implements PermissionOverride {
    private final Discord                discord;
    private final Server                 server;
    private final Type                   type;
    private final PermissionOverwritable parent;
    
    public PermissionOverrideInternal(Discord discord, Server server, JsonNode data) {
        super();
        this.discord = discord;
        this.server = server;
        this.type = Type.getByKey(data.get("type")
                                          .asText());
        switch (type) {
            default:
                throw new AssertionError();
            case ROLE:
                long roleId = data.get("id")
                        .asLong();
                this.parent = discord.getRoleCache()
                        .getOrRequest(roleId, IDPair.of(server.getId(), roleId));
                break;
            case USER:
                long userId = data.get("user_id")
                        .asLong();
                this.parent = discord.getUserCache()
                        .getOrRequest(userId, userId)
                        .toServerMember(server);
                break;
        }
        new PermissionListInternal(data.get("allow")
                                           .asInt(0)).forEach(permission -> put(permission, ALLOWED));
        new PermissionListInternal(data.get("deny")
                                           .asInt(0)).forEach(permission -> put(permission, DENIED));
    }
    
    // Override Methods
    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof PermissionOverride)) return false;
        PermissionOverride other = (PermissionOverride) o;
        
        return (getAllowed().toPermissionInt() == other.getAllowed()
                .toPermissionInt()) && (getDenied().toPermissionInt() == other.getDenied()
                .toPermissionInt());
    }
    
    @Override
    public Discord getDiscord() {
        return discord;
    }
    
    @Override
    public Server getServer() {
        return server;
    }
    
    @Override
    public Type getOverrideType() {
        return type;
    }
    
    @Override
    public PermissionOverwritable getParent() {
        return parent;
    }
    
    @Override
    public PermissionOverride addOverride(Permission permission, OverrideState state) {
        removeOverride(permission);
        put(permission, state);
        return this;
    }
    
    @Override
    public PermissionOverride removeOverride(Permission permission) {
        remove(permission);
        return this;
    }
    
    @Override
    public PermissionList getAllowed() {
        return entrySet().stream()
                .filter(entry -> entry.getValue() == ALLOWED)
                .map(Entry::getKey)
                .collect(Collectors.toCollection(() -> PermissionList.create(parent)));
    }
    
    @Override
    public PermissionList getDenied() {
        return entrySet().stream()
                .filter(entry -> entry.getValue() == DENIED)
                .map(Entry::getKey)
                .collect(Collectors.toCollection(() -> PermissionList.create(parent)));
    }
    
    public JsonNode toJsonNode() {
        return JsonHelper.objectNode("id",
                                     Long.toUnsignedString(Objects.requireNonNull(parent)
                                                                   .getId()),
                                     "type",
                                     type.getKey(),
                                     "allow",
                                     getAllowed().toPermissionInt(),
                                     "deny",
                                     getDenied().toPermissionInt());
    }
}
