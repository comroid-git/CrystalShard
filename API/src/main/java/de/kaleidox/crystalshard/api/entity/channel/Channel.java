package de.kaleidox.crystalshard.api.entity.channel;

import org.intellij.lang.annotations.MagicConstant;

import de.kaleidox.crystalshard.CoreInvoker;
import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.entity.DiscordItem;
import de.kaleidox.crystalshard.api.entity.server.Server;
import de.kaleidox.crystalshard.api.entity.server.permission.PermissionApplyable;
import de.kaleidox.crystalshard.api.exception.DiscordPermissionException;
import de.kaleidox.crystalshard.api.exception.IllegalThreadException;
import de.kaleidox.crystalshard.api.handling.listener.ListenerAttachable;
import de.kaleidox.crystalshard.api.handling.listener.channel.ChannelAttachableListener;
import de.kaleidox.crystalshard.api.util.Castable;
import de.kaleidox.crystalshard.api.util.Highlightable;
import de.kaleidox.crystalshard.core.cache.Cacheable;
import de.kaleidox.crystalshard.core.concurrent.ThreadPool;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public interface Channel extends
        DiscordItem,
        PermissionApplyable,
        Highlightable,
        Castable<Channel>,
        ListenerAttachable<ChannelAttachableListener>,
        Cacheable<Channel, Long, Long> {
    @MagicConstant(valuesFromClass = Type.class)
    int getType();

    String getName();

    CompletableFuture<Void> delete();

    @Override
    default URL getHighlighingLink() {
        try {
            return new URL("https://discordapp.com/channels/" + (
                    isPrivate()
                            ? getId()
                            : getServer().map(DiscordItem::getId).get() + getId()
            ));
        } catch (MalformedURLException e) {
            throw new AssertionError(e);
        }
    }

    default boolean isPrivate() {
        return asServerChannel().isEmpty();
    }

    default Optional<ServerChannel> asServerChannel() {
        return castTo(ServerChannel.class);
    }

    default Optional<ChannelCategory> asCategory() {
        return castTo(ChannelCategory.class);
    }

    default Optional<PrivateChannel> asPrivateChannel() {
        return castTo(PrivateChannel.class);
    }

    default Optional<TextChannel> asTextChannel() {
        return castTo(TextChannel.class);
    }

    default Optional<VoiceChannel> asVoiceChannel() {
        return castTo(VoiceChannel.class);
    }

    default Optional<GroupChannel> asGroupChannel() {
        return castTo(GroupChannel.class);
    }

    default Optional<ServerTextChannel> asServerTextChannel() {
        return castTo(ServerTextChannel.class);
    }

    default Optional<ServerVoiceChannel> asServerVoiceChannel() {
        return castTo(ServerVoiceChannel.class);
    }

    default Optional<PrivateTextChannel> asPrivateTextChannel() {
        return castTo(PrivateTextChannel.class);
    }

    default Optional<Server> getServer() {
        return asServerChannel().flatMap(ServerChannel::getServer);
    }

    static CompletableFuture<? extends Channel> fromID(long id) throws IllegalThreadException {
        return fromID(ThreadPool.getThreadDiscord(), id);
    }

    static CompletableFuture<? extends Channel> fromID(Discord discord, long id) {
        return CompletableFuture.supplyAsync(
                () -> CoreInvoker.INSTANCE.fromIDs(discord, CoreInvoker.EntityType.CHANNEL, id)
        );
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
         * Builds an instance of {@code R}. The returned future completes exceptionally with a {@link
         * DiscordPermissionException} if the bot does not have the
         * required permission to build the channel.
         *
         * @return A future that completes with the built channel.
         */
        CompletableFuture<R> build();
    }

    interface Updater<T, R> {
        Discord getDiscord();

        CompletableFuture<R> update();
    }

    class Type {
        public static final int GUILD_TEXT = 0;
        public static final int DIRECT_MESSAGE = 1;
        public static final int GUILD_VOICE = 2;
        public static final int GROUP_DM = 3;
        public static final int GUILD_CATEGORY = 4;
    }
}
