package de.kaleidox.crystalshard.main.handling.event.channel;

import de.kaleidox.crystalshard.main.items.DiscordItem;
import de.kaleidox.crystalshard.main.items.channel.*;

import java.util.Optional;

public interface OptionalChannelEvent {
    Optional<Channel> getChannel();

    default Optional<Long> getChannelId() {
        return getChannel().map(DiscordItem::getId);
    }

    default Optional<ServerTextChannel> getServerTextChannel() {
        return getChannel().flatMap(Channel::toServerTextChannel);
    }

    default Optional<ServerVoiceChannel> getServerVoiceChannel() {
        return getChannel().flatMap(Channel::toServerVoiceChannel);
    }

    default Optional<PrivateTextChannel> getPrivateTextChannel() {
        return getChannel().flatMap(Channel::toPrivateTextChannel);
    }

    default Optional<GroupChannel> getGroupChannel() {
        return getChannel().flatMap(Channel::toGroupChannel);
    }
}
