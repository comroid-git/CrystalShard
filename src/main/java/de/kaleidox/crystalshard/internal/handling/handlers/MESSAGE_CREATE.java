package de.kaleidox.crystalshard.internal.handling.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.message.generic.MessageCreateEventInternal;
import de.kaleidox.crystalshard.internal.items.channel.ChannelInternal;
import de.kaleidox.crystalshard.internal.items.message.MessageInternal;
import de.kaleidox.crystalshard.main.handling.event.message.generic.MessageCreateEvent;
import de.kaleidox.crystalshard.main.handling.listener.message.MessageCreateListener;
import de.kaleidox.crystalshard.main.items.channel.Channel;
import de.kaleidox.crystalshard.main.items.channel.ServerChannel;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.user.AuthorUser;

/**
 * https://discordapp.com/developers/docs/topics/gateway#message-create
 */
public class MESSAGE_CREATE extends HandlerBase {
    @Override
    public void handle(DiscordInternal discord, JsonNode data) {
        Channel channel = ChannelInternal.getInstance(discord, data.get("channel_id").asLong());
        Server server = channel.toServerChannel().map(ServerChannel::getServer).orElse(null);
        Message message = MessageInternal.getInstance(discord, data);
        AuthorUser user = message.getAuthorAsUser().orElse(null);

        MessageCreateEvent event = new MessageCreateEventInternal(discord, message);

        collectListeners(MessageCreateListener.class, discord, server, channel, user)
                .forEach(listener -> listener.onMessageCreate(event));
    }
}
