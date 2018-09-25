package de.kaleidox.crystalshard.main.items.channel;

import de.kaleidox.crystalshard.core.cache.CacheStorable;
import de.kaleidox.crystalshard.core.cache.Cacheable;
import de.kaleidox.crystalshard.internal.items.channel.ChannelBuilderInternal;
import de.kaleidox.crystalshard.main.handling.listener.ListenerAttachable;
import de.kaleidox.crystalshard.main.handling.listener.channel.ChannelAttachableListener;
import de.kaleidox.crystalshard.main.items.DiscordItem;
import de.kaleidox.crystalshard.main.items.permission.PermissionApplyable;
import de.kaleidox.crystalshard.main.items.permission.PermissionOverride;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.util.Castable;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public interface Channel
        extends DiscordItem, PermissionApplyable, Castable<Channel>, ListenerAttachable<ChannelAttachableListener>,
        Cacheable<Channel, Long, Long>, CacheStorable {
    ChannelType getType();
    
    String getName();
    
    int getPosition();
    
    Updater getUpdater();
    
    default boolean isPrivate() {
        return toServerChannel().isEmpty();
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
    
    default Builder BUILDER() {
        return new ChannelBuilderInternal();
    }
    
    interface Updater extends Castable<Updater> {
        Updater setName(String name);
        
        Updater setPosition(int position);
        
        Updater modifyOverrides(Consumer<List<PermissionOverride>> overrideModifier);
        
        CompletableFuture<Void> update();
    }
    
    /**
     * This interface represents a basic channel builder.
     *
     * @see ServerTextChannel#BUILDER(Server)
     * @see ServerVoiceChannel#BUILDER(Server)
     * @see ChannelCategory#BUILDER(Server)
     * @see GroupChannel#BUILDER()
     */
    interface Builder {
        /**
         * Gets a new instance of a ServerVoiceChannelBuilder. When using this method, you <b>MUST</b> later specify a
         * server using {@link ServerVoiceChannel.Builder#setServer(Server)}.
         *
         * @return A new ServerVoiceChannelBuilder.
         */
        default ServerVoiceChannel.Builder makeVoiceChannel() {
            return new ChannelBuilderInternal.ServerVoiceChannelBuilder(null);
        }
        
        /**
         * Gets a new instance of a ServerTextChannelBuilder. When using this method, you <b>MUST</b> later specify a
         * server using {@link ServerTextChannel.Builder#setServer(Server)}.
         *
         * @return A new ServerTextChannelBuilder.
         */
        default ServerTextChannel.Builder makeServerChannel() {
            return new ChannelBuilderInternal.ServerTextChannelBuilder(null);
        }
        
        /**
         * Gets a new instance of a ChannelCategoryBuilder. When using this method, you <b>MUST</b> later specify a
         * server using {@link ServerVoiceChannel.Builder#setServer(Server)}.
         *
         * @return A new ChannelCategoryBuilder.
         */
        default ChannelCategory.Builder makeChannelCategory() {
            return new ChannelBuilderInternal.ChannelCategoryBuilder(null);
        }
        
        /**
         * Gets a new instance of a GroupChannelBuilder.
         *
         * @return A new GroupChannelBuilder.
         */
        default GroupChannel.Builder makeGroupChannel() {
            return new ChannelBuilderInternal.GroupChannelBuilder(null);
        }
    }
}
