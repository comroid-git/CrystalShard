package de.kaleidox.crystalshard.internal.handling.handlers;

import com.fasterxml.jackson.databind.JsonNode;

import de.kaleidox.crystalshard.api.entity.channel.ServerChannel;
import de.kaleidox.crystalshard.api.entity.channel.TextChannel;
import de.kaleidox.crystalshard.api.entity.role.Role;
import de.kaleidox.crystalshard.api.entity.server.Server;
import de.kaleidox.crystalshard.api.entity.user.User;
import de.kaleidox.crystalshard.api.handling.listener.channel.other.TypingStartListener;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.channel.other.TypingStartEventInternal;
import de.kaleidox.crystalshard.internal.util.RoleContainer;

import java.util.Collection;
import java.util.Collections;

public class TYPING_START extends HandlerBase {
    // Override Methods
    @Override
    public void handle(DiscordInternal discord, JsonNode data) {
        long channelId = data.get("channel_id")
                .asLong();
        TextChannel channel = discord.getChannelCache()
                .getOrRequest(channelId, channelId)
                .toTextChannel()
                .orElseThrow(AssertionError::new);
        Server server = channel.toServerChannel()
                .map(ServerChannel::getServer)
                .orElse(null);
        long userId = data.get("user_id")
                .asLong();
        User user = discord.getUserCache()
                .getOrRequest(userId, userId);
        Collection<Role> roles = (user != null ? user.getRoles(server) : Collections.emptyList());

        TypingStartEventInternal event = new TypingStartEventInternal(discord, channel, user);

        collectListeners(TypingStartListener.class, discord, server, channel, new RoleContainer(roles), user).forEach(listener -> discord.getThreadPool()
                .execute(() -> listener.onTypingStart(event)));
    }
}
