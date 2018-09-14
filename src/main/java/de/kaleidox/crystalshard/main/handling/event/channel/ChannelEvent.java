package de.kaleidox.crystalshard.main.handling.event.channel;

import de.kaleidox.crystalshard.main.handling.event.Event;
import de.kaleidox.crystalshard.main.items.channel.*;

import java.util.Optional;

public interface ChannelEvent extends Event {
    Channel getChannel();

    default long getChannelId() {
        return getChannel().getId();
    }

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
