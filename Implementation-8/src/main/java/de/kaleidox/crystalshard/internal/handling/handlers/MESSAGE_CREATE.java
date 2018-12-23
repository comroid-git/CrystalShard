package de.kaleidox.crystalshard.internal.handling.handlers;

import com.fasterxml.jackson.databind.JsonNode;

import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.message.generic.MessageCreateEventInternal;
import de.kaleidox.crystalshard.internal.util.RoleContainer;
import de.kaleidox.crystalshard.main.handling.event.message.generic.MessageCreateEvent;
import de.kaleidox.crystalshard.main.handling.listener.message.generic.MessageCreateListener;
import de.kaleidox.crystalshard.main.items.channel.Channel;
import de.kaleidox.crystalshard.main.items.channel.ServerChannel;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.items.role.Role;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.user.User;

import java.util.Collection;
import java.util.Collections;

/**
 * https://discordapp.com/developers/docs/topics/gateway#message-create
 */
public class MESSAGE_CREATE extends HandlerBase {
    // Override Methods
    @Override
    public void handle(DiscordInternal discord, JsonNode data) {
        long channel_id = data.get("channel_id")
                .asLong();
        Channel channel = discord.getChannelCache()
                .getOrRequest(channel_id, channel_id);
        Server server = channel.toServerChannel()
                .map(ServerChannel::getServer)
                .orElse(null);
        Message message = discord.getMessageCache()
                .getOrCreate(discord, data);
        User user = message.getAuthorAsUser()
                .orElse(null);
        Collection<Role> roles = (user != null ? user.getRoles(server) : Collections.emptyList());

        MessageCreateEvent event = new MessageCreateEventInternal(discord, message);

        collectListeners(MessageCreateListener.class, discord, server, channel, user, new RoleContainer(roles)).forEach(listener -> discord.getThreadPool()
                .execute(() -> listener.onMessageCreate(event)));
    }
}
