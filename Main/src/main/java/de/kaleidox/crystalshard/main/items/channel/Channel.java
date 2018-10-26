package de.kaleidox.crystalshard.main.items.channel;

import de.kaleidox.crystalshard.core.cache.Cacheable;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.exception.DiscordPermissionException;
import de.kaleidox.crystalshard.main.handling.listener.ListenerAttachable;
import de.kaleidox.crystalshard.main.handling.listener.channel.ChannelAttachableListener;
import de.kaleidox.crystalshard.main.items.DiscordItem;
import de.kaleidox.crystalshard.main.items.permission.PermissionApplyable;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.util.Castable;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public interface Channel
        extends DiscordItem, PermissionApplyable, Castable<Channel>, ListenerAttachable<ChannelAttachableListener>, Cacheable<Channel, Long, Long> {
    ChannelType getType();
    
    String getName();
    
    int getPosition();
    
    CompletableFuture<Void> delete();
    
    default boolean isPrivate() {
        return !toServerChannel().isPresent();
    }
    
    default Optional<ChannelCategory> toChannelCategory() {
        return castTo(ChannelCategory.class);
    }
    
    default Optional<ServerChannel> toServerChannel() {
        return castTo(ServerChannel.class);
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
