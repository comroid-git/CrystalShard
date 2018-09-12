package de.kaleidox.crystalshard.internal.handling.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.message.reaction.ReactionAddEventInternal;
import de.kaleidox.crystalshard.internal.items.channel.ChannelInternal;
import de.kaleidox.crystalshard.internal.items.message.MessageInternal;
import de.kaleidox.crystalshard.internal.items.message.reaction.ReactionInternal;
import de.kaleidox.crystalshard.internal.items.user.UserInternal;
import de.kaleidox.crystalshard.main.handling.listener.message.reaction.ReactionAddListener;
import de.kaleidox.crystalshard.main.items.channel.ServerChannel;
import de.kaleidox.crystalshard.main.items.channel.TextChannel;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.items.message.reaction.Reaction;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.user.User;

public class MESSAGE_REACTION_ADD extends HandlerBase {
    @Override
    public void handle(DiscordInternal discord, JsonNode data) {
        TextChannel channel = ChannelInternal.getInstance(discord, data.get("channel_id").asLong()).toTextChannel().get();
        Message message = MessageInternal.getInstance(channel, data.get("message_id").asLong());
        Server server = channel.toServerChannel().map(ServerChannel::getServer).orElse(null);
        User user = UserInternal.getInstance(discord, data.get("user_id").asLong());

        Reaction reaction = ReactionInternal.getInstance(server, message, user, data, 1);
        ReactionAddEventInternal event = new ReactionAddEventInternal(discord, reaction, message);

        collectListeners(ReactionAddListener.class, null, discord, server, channel, message)
                .forEach(listener -> discord.getThreadPool().execute(() -> listener.onEvent(event)));
    }
}