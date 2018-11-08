package de.kaleidox.crystalshard.internal.handling.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.message.reaction.ReactionRemoveEventInternal;
import de.kaleidox.crystalshard.internal.items.message.reaction.ReactionInternal;
import de.kaleidox.crystalshard.main.handling.event.message.reaction.ReactionRemoveEvent;
import de.kaleidox.crystalshard.main.handling.listener.message.reaction.ReactionRemoveListener;
import de.kaleidox.crystalshard.main.items.channel.ServerChannel;
import de.kaleidox.crystalshard.main.items.channel.TextChannel;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.items.message.reaction.Reaction;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.user.User;
import de.kaleidox.util.objects.markers.IDPair;

public class MESSAGE_REACTION_REMOVE extends HandlerBase {
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

        Reaction reaction = ReactionInternal.getInstance(server, message, user, data, -1);
        ReactionRemoveEvent event = new ReactionRemoveEventInternal(discord, reaction, message);

        collectListeners(ReactionRemoveListener.class, discord, server, channel, message).forEach(listener -> discord.getThreadPool()
                .execute(() -> listener.onReactionRemove(event)));
    }
}
