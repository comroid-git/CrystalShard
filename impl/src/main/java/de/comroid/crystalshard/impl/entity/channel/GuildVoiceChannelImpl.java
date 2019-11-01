package de.comroid.crystalshard.impl.entity.channel;

import de.comroid.crystalshard.api.entity.channel.GuildVoiceChannel;
import de.comroid.crystalshard.core.annotation.JsonData;

public class GuildVoiceChannelImpl extends AbstractTextChannel<GuildVoiceChannelImpl> implements GuildVoiceChannel {
    // AbstractVoiceChannel
    protected @JsonData("bitrate") int bitrate;
    protected @JsonData("user_limit") int userLimit;
}
