package de.kaleidox.crystalshard.internal.handling.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.channel.generic.ChannelEditEventInternal;
import de.kaleidox.crystalshard.internal.items.channel.ChannelInternal;
import de.kaleidox.crystalshard.main.handling.editevent.EditTrait;
import de.kaleidox.crystalshard.main.handling.listener.channel.generic.ChannelEditListener;
import de.kaleidox.crystalshard.main.items.channel.Channel;
import de.kaleidox.crystalshard.main.items.channel.ServerChannel;
import de.kaleidox.crystalshard.main.items.server.Server;

import java.util.Set;

/**
 * https://discordapp.com/developers/docs/topics/gateway#channel-update
 */
public class CHANNEL_UPDATE extends HandlerBase {
// Override Methods
    @Override
    public void handle(DiscordInternal discord, JsonNode data) {
        ChannelInternal channel = (ChannelInternal) ChannelInternal.getInstance(discord, data);
        Server server = channel.toServerChannel().map(ServerChannel::getServer).orElse(null);
        Set<EditTrait<Channel>> traits = channel.updateData(data);
        
        ChannelEditEventInternal event = new ChannelEditEventInternal(discord, channel, traits);
        
        collectListeners(ChannelEditListener.class,
                         discord,
                         server,
                         channel).forEach(listener -> discord.getThreadPool()
                .execute(() -> listener.onChannelEdit(event)));
    }
}
