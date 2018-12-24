package de.kaleidox.crystalshard.api.handling.event.channel;

import de.kaleidox.crystalshard.api.entity.channel.Channel;
import de.kaleidox.crystalshard.api.entity.channel.GroupChannel;
import de.kaleidox.crystalshard.api.entity.channel.PrivateTextChannel;
import de.kaleidox.crystalshard.api.entity.channel.ServerTextChannel;
import de.kaleidox.crystalshard.api.entity.channel.ServerVoiceChannel;
import de.kaleidox.crystalshard.api.handling.event.Event;

import java.util.Optional;

public interface ChannelEvent extends Event {
    default long getChannelId() {
        return getChannel().getId();
    }

    Channel getChannel();

    default Optional<ServerTextChannel> getServerTextChannel() {
        return getChannel().toServerTextChannel();
    }

    default Optional<ServerVoiceChannel> getServerVoiceChannel() {
        return getChannel().toServerVoiceChannel();
    }

    default Optional<PrivateTextChannel> getPrivateTextChannel() {
        return getChannel().toPrivateTextChannel();
    }

    default Optional<GroupChannel> getGroupChannel() {
        return getChannel().toGroupChannel();
    }
}
