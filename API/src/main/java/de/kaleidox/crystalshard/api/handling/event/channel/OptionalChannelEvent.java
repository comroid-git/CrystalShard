package de.kaleidox.crystalshard.api.handling.event.channel;

import de.kaleidox.crystalshard.api.entity.DiscordItem;
import de.kaleidox.crystalshard.api.entity.channel.Channel;
import de.kaleidox.crystalshard.api.entity.channel.GroupChannel;
import de.kaleidox.crystalshard.api.entity.channel.PrivateTextChannel;
import de.kaleidox.crystalshard.api.entity.channel.ServerTextChannel;
import de.kaleidox.crystalshard.api.entity.channel.ServerVoiceChannel;

import java.util.Optional;

public interface OptionalChannelEvent {
    default Optional<Long> getChannelId() {
        return getChannel().map(DiscordItem::getId);
    }

    Optional<Channel> getChannel();

    default Optional<ServerTextChannel> getServerTextChannel() {
        return getChannel().flatMap(Channel::asServerTextChannel);
    }

    default Optional<ServerVoiceChannel> getServerVoiceChannel() {
        return getChannel().flatMap(Channel::asServerVoiceChannel);
    }

    default Optional<PrivateTextChannel> getPrivateTextChannel() {
        return getChannel().flatMap(Channel::asPrivateTextChannel);
    }

    default Optional<GroupChannel> getGroupChannel() {
        return getChannel().flatMap(Channel::asGroupChannel);
    }
}
