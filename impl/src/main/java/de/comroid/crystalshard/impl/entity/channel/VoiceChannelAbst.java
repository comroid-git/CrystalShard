package de.comroid.crystalshard.impl.entity.channel;

import com.fasterxml.jackson.databind.JsonNode;
import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.api.entity.channel.VoiceChannel;

public class VoiceChannelAbst<Self extends VoiceChannelAbst<Self>> extends ChannelAbst<Self> implements VoiceChannel {
    protected VoiceChannelAbst(Discord api, JsonNode data) {
        super(api, data);
    }
}
