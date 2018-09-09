package de.kaleidox.crystalshard.main.items.channel;

import de.kaleidox.crystalshard.internal.items.channel.ChannelBuilderInternal;
import de.kaleidox.crystalshard.main.handling.listener.ListenerAttachable;
import de.kaleidox.crystalshard.main.handling.listener.channel.ChannelAttachableListener;
import de.kaleidox.crystalshard.main.items.DiscordItem;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.util.Castable;

import java.util.Optional;

@SuppressWarnings("unused")
public interface Channel extends DiscordItem, Castable<Channel>, ListenerAttachable<ChannelAttachableListener> {
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

    ChannelType getType();

    default Builder BUILDER() {
        return new ChannelBuilderInternal();
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
         * Gets a new instance of a ServerVoiceChannelBuilder.
         *
         * @return A new ServerVoiceChannelBuilder.
         * @implNote When using this method, you <b>MUST</b> later specify a server using {@link ServerVoiceChannel.Builder#setServer(Server)}.
         */
        default ServerVoiceChannel.Builder makeVoiceChannel() {
            return new ChannelBuilderInternal.ServerVoiceChannelBuilder(null);
        }

        /**
         * Gets a new instance of a ServerTextChannelBuilder.
         *
         * @return A new ServerTextChannelBuilder.
         * @implNote When using this method, you <b>MUST</b> later specify a server using {@link ServerTextChannel.Builder#setServer(Server)}.
         */
        default ServerTextChannel.Builder makeServerChannel() {
            return new ChannelBuilderInternal.ServerTextChannelBuilder(null);
        }

        /**
         * Gets a new instance of a ChannelCategoryBuilder.
         *
         * @return A new ChannelCategoryBuilder.
         * @implNote When using this method, you <b>MUST</b> later specify a server using {@link ChannelCategory.Builder#setServer(Server)}.
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
