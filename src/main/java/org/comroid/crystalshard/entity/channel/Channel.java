package org.comroid.crystalshard.entity.channel;

import org.comroid.common.ref.Specifiable;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.model.Mentionable;

import java.util.Optional;

public interface Channel extends Snowflake, Mentionable, Specifiable<Channel> {
    @Override
    default String getDefaultFormattedName() {
        return toString();
    }

    @Override
    default String getAlternateFormattedName() {
        return getMentionTag();
    }

    default Optional<TextChannel> asTextChannel() {
        return as(TextChannel.class);
    }

    default Optional<VoiceChannel> asVoiceChannel() {
        return as(VoiceChannel.class);
    }

    default Optional<GuildChannel> asGuildChannel() {
        return as(GuildChannel.class);
    }

    default Optional<PrivateChannel> asPrivateChannel() {
        return as(PrivateChannel.class);
    }

    default Optional<GuildTextChannel> asGuildTextChannel() {
        return as(GuildTextChannel.class);
    }

    default Optional<GuildVoiceChannel> asGuildVoiceChannel() {
        return as(GuildVoiceChannel.class);
    }

    default Optional<PrivateTextChannel> asPrivateTextChannel() {
        return as(PrivateTextChannel.class);
    }
}
