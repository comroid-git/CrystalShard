package de.kaleidox.crystalshard.main.items.role;

import de.kaleidox.crystalshard.core.cache.Cacheable;
import de.kaleidox.crystalshard.core.concurrent.ThreadPool;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.exception.IllegalThreadException;
import de.kaleidox.crystalshard.main.handling.listener.ListenerAttachable;
import de.kaleidox.crystalshard.main.handling.listener.server.role.RoleAttachableListener;
import de.kaleidox.crystalshard.main.items.DiscordItem;
import de.kaleidox.crystalshard.main.items.Mentionable;
import de.kaleidox.crystalshard.main.items.Nameable;
import de.kaleidox.crystalshard.main.items.permission.PermissionList;
import de.kaleidox.crystalshard.main.items.permission.PermissionOverride;
import de.kaleidox.crystalshard.main.items.permission.PermissionOverwritable;
import de.kaleidox.util.objects.markers.IDPair;

import java.awt.*;
import java.util.concurrent.CompletableFuture;

public interface Role
        extends DiscordItem, Nameable, Mentionable, PermissionOverwritable, ListenerAttachable<RoleAttachableListener>, Cacheable<Role, Long, IDPair>,
        Comparable<Role> {
    static Role getFromId(Discord discord, long id) {
        return discord.getRoleCache()
                .get(id);
    }

    static Role getFromId(long id) throws IllegalThreadException {
        return getFromId(ThreadPool.getThreadDiscord(), id);
    }

    Color getColor();

    boolean isGrouping();

    boolean isManaged();

    boolean isMentionable();

    int getPosition();

    PermissionList getPermissions();

    CompletableFuture<Void> delete();

    interface Builder {
        Builder setName(String name);

        Builder setPermissions(PermissionOverride permissionOverride);

        Builder setColor(Color color);

        Builder setHoist(boolean hoist);

        Builder setMentionable(boolean mentionable);

        CompletableFuture<Role> build();
    }
}
