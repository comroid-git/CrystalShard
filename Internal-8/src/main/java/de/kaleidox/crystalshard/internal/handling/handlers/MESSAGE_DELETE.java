package de.kaleidox.crystalshard.internal.handling.handlers;

import com.fasterxml.jackson.databind.JsonNode;

import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.message.generic.MessageDeleteEventInternal;
import de.kaleidox.crystalshard.internal.util.RoleContainer;
import de.kaleidox.crystalshard.main.handling.listener.message.generic.MessageDeleteListener;
import de.kaleidox.crystalshard.main.items.channel.TextChannel;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.items.role.Role;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.user.User;
import de.kaleidox.util.objects.markers.IDPair;

import java.util.Collection;
import java.util.Collections;

/**
 * https://discordapp.com/developers/docs/topics/gateway#message-delete
 */
public class MESSAGE_DELETE extends HandlerBase {
    @Override
    public void handle(DiscordInternal discord, JsonNode data) {
        long serverId = data.path("guild_id")
                .asLong(-1);
        long channelId = data.get("channel_id")
                .asLong();
        long messageId = data.get("id")
                .asLong();
        TextChannel channel = discord.getChannelCache()
                .getOrRequest(channelId, channelId)
                .toTextChannel()
                .orElseThrow(AssertionError::new);
        Message message = discord.getMessageCache()
                .getOrRequest(messageId, IDPair.of(channelId, messageId));
        Server server = serverId != -1 ? discord.getServerCache()
                .getOrRequest(serverId, serverId) : null;
        User user = message.getAuthorAsUser()
                .orElse(null);
        Collection<Role> roles = (user != null ? user.getRoles(server) : Collections.emptyList());

        MessageDeleteEventInternal event = new MessageDeleteEventInternal(discord, message);

        collectListeners(MessageDeleteListener.class,
                discord,
                server,
                channel,
                new RoleContainer(roles),
                user,
                message).forEach(listener -> discord.getThreadPool()
                .execute(() -> listener.onMessageDelete(event)));

        message.detachAllListeners(); // take this, basti
    }
}
