package de.kaleidox.crystalshard.api.entity.server;

import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.entity.channel.VoiceChannel;
import de.kaleidox.crystalshard.api.entity.user.User;

import java.util.Optional;

public interface VoiceState {
    Discord getDiscord();

    Optional<Server> getServer();

    VoiceChannel getChannel();

    User getUser();

    boolean isDeafened();

    boolean isMuted();

    boolean isSelfDeafened();

    boolean isSelfMuted();

    boolean isSuppressed();
}
