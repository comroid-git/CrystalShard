package de.kaleidox.crystalshard.main.items.role;

import de.kaleidox.crystalshard.core.cache.CacheStorable;
import de.kaleidox.crystalshard.core.cache.Cacheable;
import de.kaleidox.crystalshard.main.handling.listener.ListenerAttachable;
import de.kaleidox.crystalshard.main.handling.listener.server.role.RoleAttachableListener;
import de.kaleidox.crystalshard.main.items.DiscordItem;
import de.kaleidox.crystalshard.main.items.Mentionable;
import de.kaleidox.crystalshard.main.items.Nameable;
import de.kaleidox.crystalshard.main.items.permission.PermissionList;
import de.kaleidox.crystalshard.main.items.permission.PermissionOverwritable;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.util.objects.markers.IDPair;
import java.awt.Color;
import java.util.concurrent.CompletableFuture;

public interface Role
        extends DiscordItem, Nameable, Mentionable, PermissionOverwritable, ListenerAttachable<RoleAttachableListener>, Cacheable<Role, Long, IDPair>,
        CacheStorable {
    CompletableFuture<Server> getServer();
    
    Color getColor();
    
    boolean isGrouping();
    
    boolean isManaged();
    
    boolean isMentionable();
    
    int getPosition();
    
    PermissionList getPermissions();
}
