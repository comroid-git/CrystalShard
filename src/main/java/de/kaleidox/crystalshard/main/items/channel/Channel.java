package de.kaleidox.crystalshard.main.items.channel;

import de.kaleidox.crystalshard.main.items.DiscordItem;
import de.kaleidox.crystalshard.main.util.Castable;

import java.util.Optional;

public interface Channel extends DiscordItem, Castable<Channel> {
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

    default Optional<PrivateVoiceChannel> toPrivateVoiceChannel() {
        return castTo(PrivateVoiceChannel.class);
    }
}
