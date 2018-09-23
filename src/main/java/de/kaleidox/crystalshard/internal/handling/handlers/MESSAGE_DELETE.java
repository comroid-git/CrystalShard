package de.kaleidox.crystalshard.internal.handling.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.message.generic.MessageDeleteEventInternal;
import de.kaleidox.crystalshard.internal.items.channel.TextChannelInternal;
import de.kaleidox.crystalshard.internal.items.message.MessageInternal;
import de.kaleidox.crystalshard.internal.items.server.ServerInternal;
import de.kaleidox.crystalshard.main.handling.listener.message.generic.MessageDeleteListener;
import de.kaleidox.crystalshard.main.items.channel.TextChannel;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.items.role.Role;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.user.User;

import java.util.Collection;
import java.util.Collections;

/**
 * https://discordapp.com/developers/docs/topics/gateway#message-delete
 */
public class MESSAGE_DELETE extends HandlerBase {
    // Override Methods
    @Override
    public void handle(DiscordInternal discord, JsonNode data) {
        TextChannel channel = TextChannelInternal.getInstance(discord,
                                                              data.get("channel_id")
                                                                      .asLong())
                .toTextChannel()
                .orElseThrow(AssertionError::new);
        Message message = MessageInternal.getInstance(channel,
                                                      data.get("id")
                                                              .asLong());
        Server server = data.has("guild_id") ? ServerInternal.getInstance(discord,
                                                                          data.get("guild_id")
                                                                                  .asLong()) : null;
        User user = message.getAuthorAsUser()
                .orElse(null);
        Collection<Role> roles = (user != null ? user.getRoles(server) : Collections.emptyList());
        
        MessageDeleteEventInternal event = new MessageDeleteEventInternal(discord, message);
        
        collectListeners(MessageDeleteListener.class,
                         discord,
                         server,
                         channel,
                         roles.toArray(new Role[0]),
                         user,
                         message).forEach(listener -> discord.getThreadPool()
                .execute(() -> listener.onMessageDelete(event)));
        
        message.detachAllListeners(); // take this, basti
    }
}
