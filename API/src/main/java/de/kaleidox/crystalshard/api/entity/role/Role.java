package de.kaleidox.crystalshard.api.entity.role;

import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.entity.DiscordItem;
import de.kaleidox.crystalshard.api.entity.Mentionable;
import de.kaleidox.crystalshard.api.entity.Nameable;
import de.kaleidox.crystalshard.api.entity.server.permission.PermissionList;
import de.kaleidox.crystalshard.api.entity.server.permission.PermissionOverride;
import de.kaleidox.crystalshard.api.entity.server.permission.PermissionOverwritable;
import de.kaleidox.crystalshard.api.exception.IllegalThreadException;
import de.kaleidox.crystalshard.api.handling.listener.ListenerAttachable;
import de.kaleidox.crystalshard.api.handling.listener.server.role.RoleAttachableListener;
import de.kaleidox.crystalshard.core.cache.Cacheable;
import de.kaleidox.crystalshard.core.concurrent.ThreadPool;
import de.kaleidox.util.markers.IDPair;

import java.awt.Color;
import java.util.concurrent.CompletableFuture;

public interface Role
        extends DiscordItem, Nameable, Mentionable, PermissionOverwritable, ListenerAttachable<RoleAttachableListener>, Cacheable<Role, Long, IDPair>,
        Comparable<Role> {
    Color getColor();

    boolean isGrouping();

    boolean isManaged();

    boolean isMentionable();

    int getPosition();

    PermissionList getPermissions();

    CompletableFuture<Void> delete();

    static Role getFromId(long id) throws IllegalThreadException {
        return getFromId(ThreadPool.getThreadDiscord(), id);
    }

    static Role getFromId(Discord discord, long id) {
        return discord.getRoleCache()
                .get(id);
    }

    interface Builder {
        Builder setName(String name);

        Builder setPermissions(PermissionOverride permissionOverride);

        Builder setColor(Color color);

        Builder setHoist(boolean hoist);

        Builder setMentionable(boolean mentionable);

        CompletableFuture<Role> build();
    }
}
