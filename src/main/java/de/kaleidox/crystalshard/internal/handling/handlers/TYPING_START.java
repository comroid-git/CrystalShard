package de.kaleidox.crystalshard.internal.handling.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.channel.other.TypingStartEventInternal;
import de.kaleidox.crystalshard.internal.items.channel.ChannelInternal;
import de.kaleidox.crystalshard.internal.items.user.UserInternal;
import de.kaleidox.crystalshard.main.handling.listener.channel.other.TypingStartListener;
import de.kaleidox.crystalshard.main.items.channel.ServerChannel;
import de.kaleidox.crystalshard.main.items.channel.TextChannel;
import de.kaleidox.crystalshard.main.items.role.Role;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.user.User;

import java.util.Collection;
import java.util.Collections;

public class TYPING_START extends HandlerBase {
    @Override
    public void handle(DiscordInternal discord, JsonNode data) {
        TextChannel channel = ChannelInternal.getInstance(discord, data.get("channel_id").asLong())
                .toTextChannel()
                .orElseThrow(AssertionError::new);
        Server server = channel.toServerChannel().map(ServerChannel::getServer).orElse(null);
        User user = UserInternal.getInstance(discord, data.get("user_id").asLong());
        Collection<Role> roles = (user != null ? user.getRoles(server) : Collections.emptyList());

        TypingStartEventInternal event = new TypingStartEventInternal(discord, channel, user);

        collectListeners(TypingStartListener.class,
                discord, server, channel, roles.toArray(new Role[0]), user)
                .forEach(listener -> discord.getThreadPool()
                        .execute(() -> listener.onTypingStart(event))
                );
    }
}
