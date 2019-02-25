package de.kaleidox.crystalshard.internal.handling.handlers;

import com.fasterxml.jackson.databind.JsonNode;

import de.kaleidox.crystalshard.api.entity.channel.ServerChannel;
import de.kaleidox.crystalshard.api.entity.channel.TextChannel;
import de.kaleidox.crystalshard.api.entity.message.Message;
import de.kaleidox.crystalshard.api.entity.message.Reaction;
import de.kaleidox.crystalshard.api.entity.server.Server;
import de.kaleidox.crystalshard.api.entity.user.User;
import de.kaleidox.crystalshard.api.handling.listener.message.reaction.ReactionAddListener;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.message.reaction.ReactionAddEventInternal;
import de.kaleidox.crystalshard.internal.items.message.reaction.ReactionInternal;
import de.kaleidox.util.markers.IDPair;

public class MESSAGE_REACTION_ADD extends HandlerBase {
    // Override Methods
    @Override
    public void handle(DiscordInternal discord, JsonNode data) {
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
        Server server = channel.toServerChannel()
                .map(ServerChannel::getServer)
                .orElse(null);
        long userId = data.get("user_id")
                .asLong();
        User user = discord.getUserCache()
                .getOrRequest(userId, userId);

        Reaction reaction = ReactionInternal.getInstance(server, message, user, data, 1);
        ReactionAddEventInternal event = new ReactionAddEventInternal(discord, reaction, message);

        collectListeners(ReactionAddListener.class, discord, server, channel, message).forEach(listener -> discord.getThreadPool()
                .execute(() -> listener.onReactionAdd(event)));
    }
}
