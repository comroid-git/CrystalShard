package de.kaleidox.crystalshard.impl.entity.channel;

import de.kaleidox.crystalshard.api.entity.channel.GuildVoiceChannel;
import de.kaleidox.crystalshard.core.annotation.JsonData;

public class GuildVoiceChannelImpl extends AbstractTextChannel<GuildVoiceChannelImpl> implements GuildVoiceChannel {
    // AbstractVoiceChannel
    protected @JsonData("bitrate") int bitrate;
    protected @JsonData("user_limit") int userLimit;
}
