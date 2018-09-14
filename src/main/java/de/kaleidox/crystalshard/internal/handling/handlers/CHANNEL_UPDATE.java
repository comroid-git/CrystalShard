package de.kaleidox.crystalshard.internal.handling.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.items.channel.*;
import de.kaleidox.crystalshard.main.handling.editevent.enums.ChannelEditTrait;
import de.kaleidox.crystalshard.main.items.channel.Channel;

import java.util.Set;

/**
 * https://discordapp.com/developers/docs/topics/gateway#channel-update
 */
public class CHANNEL_UPDATE extends HandlerBase {
    @Override
    public void handle(DiscordInternal discord, JsonNode data) {
        Channel channel = ChannelInternal.getInstance(discord, data);
        Set<ChannelEditTrait> traits;

        switch (channel.getType()) {
            case UNKNOWN:
                throw new AssertionError();
            case GUILD_TEXT:
                traits = ((ServerTextChannelInternal) channel).updateData(data);
                break;
            case DM:
                traits = ((PrivateTextChannelInternal) channel).updateData(data);
                break;
            case GUILD_VOICE:
                traits = ((ServerVoiceChannelInternal) channel).updateData(data);
                break;
            case GROUP_DM:
                traits = ((GroupChannelInternal) channel).updateData(data);
                break;
            case GUILD_CATEGORY:
                traits = ((ChannelCategoryInternal) channel).updateData(data);
                break;
        }
    }
}
