package de.kaleidox.crystalshard.api.model.voice;

import java.util.Optional;

import de.kaleidox.crystalshard.api.entity.channel.VoiceChannel;
import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.api.entity.user.GuildMember;
import de.kaleidox.crystalshard.api.entity.user.User;

public interface VoiceState {
    Optional<Guild> getGuild();

    Optional<VoiceChannel> getChannel();

    User getUser();

    default Optional<GuildMember> getGuildMember() {
        return getGuild().flatMap(getUser()::asGuildMember);
    }

    String getSessionID();

    boolean isDeafened();

    boolean isMuted();

    boolean isSelfDeafened();

    boolean isSelfMuted();

    boolean isSuppressed();
}
