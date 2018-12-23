package de.kaleidox.crystalshard.api.entity.channel;

import org.intellij.lang.annotations.MagicConstant;

import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.entity.DiscordItem;
import de.kaleidox.crystalshard.api.entity.permission.PermissionApplyable;
import de.kaleidox.crystalshard.api.entity.server.Server;
import de.kaleidox.crystalshard.api.exception.DiscordPermissionException;
import de.kaleidox.crystalshard.api.exception.IllegalThreadException;
import de.kaleidox.crystalshard.api.handling.listener.ListenerAttachable;
import de.kaleidox.crystalshard.api.handling.listener.channel.ChannelAttachableListener;
import de.kaleidox.crystalshard.api.util.Castable;
import de.kaleidox.crystalshard.core.cache.Cacheable;
import de.kaleidox.crystalshard.core.concurrent.ThreadPool;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public interface Channel
        extends DiscordItem, PermissionApplyable, Castable<Channel>, ListenerAttachable<ChannelAttachableListener>, Cacheable<Channel, Long, Long> {
    @MagicConstant(valuesFromClass = Type.class)
    int getType();

    class Type {
        public static final int GUILD_TEXT = 0;
        public static final int DIRECT_MESSAGE = 1;
        public static final int GUILD_VOIDE = 2;
        public static final int GROUP_DM = 3;
        public static final int GUILD_CATEGORY = 4;
    }

    String getName();

    int getPosition();

    CompletableFuture<Void> delete();

    default boolean isPrivate() {
        return !toServerChannel().isPresent();
    }

    default Optional<ServerChannel> toServerChannel() {
        return castTo(ServerChannel.class);
    }

    default Optional<ChannelCategory> toChannelCategory() {
        return castTo(ChannelCategory.class);
    }

    default Optional<PrivateChannel> toPrivateChannel() {
        return castTo(PrivateChannel.class);
    }

    default Optional<TextChannel> toTextChannel() {
        return castTo(TextChannel.class);
    }

    default Optional<VoiceChannel> toVoiceChannel() {
        return castTo(VoiceChannel.class);
    }

    default Optional<GroupChannel> toGroupChannel() {
        return castTo(GroupChannel.class);
    }

    default Optional<ServerTextChannel> toServerTextChannel() {
        return castTo(ServerTextChannel.class);
    }

    default Optional<ServerVoiceChannel> toServerVoiceChannel() {
        return castTo(ServerVoiceChannel.class);
    }

    default Optional<PrivateTextChannel> toPrivateTextChannel() {
        return castTo(PrivateTextChannel.class);
    }

    default Optional<Server> getServerOfChannel() {
        return toServerChannel().map(ServerChannel::getServer);
    }

    static Channel getFromId(long id) throws IllegalThreadException {
        return getFromId(ThreadPool.getThreadDiscord(), id);
    }

    static Channel getFromId(Discord discord, long id) {
        return discord.getChannelCache()
                .get(id);
    }

    interface Updater<T, R> {
        Discord getDiscord();

        CompletableFuture<R> update();
    }

    /**
     * This interface represents a basic channel builder.
     *
     * @param <T> The supertype of the Builder.
     * @param <R> The type of what the builder builds.
     */
    interface Builder<T, R> {
        Discord getDiscord();

        /**
         * Builds an instance of {@code R}. The returned future completes exceptionally with a {@link DiscordPermissionException} if the bot does not have the
         * required permission to build the channel.
         *
         * @return A future that completes with the built channel.
         */
        CompletableFuture<R> build();
    }
}
