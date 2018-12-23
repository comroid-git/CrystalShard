package de.kaleidox.crystalshard.api.entity.channel;

import de.kaleidox.crystalshard.api.entity.Nameable;
import de.kaleidox.crystalshard.api.entity.permission.PermissionOverride;
import de.kaleidox.crystalshard.api.entity.server.Server;
import de.kaleidox.crystalshard.api.entity.server.ServerComponent;
import de.kaleidox.crystalshard.api.entity.server.interactive.Invite;
import de.kaleidox.crystalshard.api.entity.server.interactive.MetaInvite;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public interface ServerChannel extends Channel, Nameable {
    Server getServer();

    Optional<ChannelCategory> getCategory();

    List<PermissionOverride> getPermissionOverrides();

    CompletableFuture<Collection<MetaInvite>> getChannelInvites();

    InviteBuilder getInviteBuilder();

    interface InviteBuilder {
        InviteBuilder setMaxAge(int maxAge);

        InviteBuilder setMaxUses(int maxUses);

        InviteBuilder setTemporaryMembership(boolean temporary);

        InviteBuilder setUnique(boolean unique);

        CompletableFuture<Invite> build();
    }

    interface Updater<T, R> extends Channel.Updater<T, R> {
        T setName(String name);

        T setPosition(int position);

        T setCategory(ChannelCategory category);

        T modifyOverrides(Consumer<List<PermissionOverride>> overrideModifier);
    }

    interface Builder<T, R> extends Channel.Builder<T, R>, ServerComponent {
        T setServer(Server server);

        T setName(String name);

        T setCategory(ChannelCategory category);

        T addPermissionOverride(PermissionOverride override);
    }
}
