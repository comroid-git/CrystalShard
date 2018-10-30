package de.kaleidox.crystalshard.internal.handling.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.message.generic.MessageEditEventInternal;
import de.kaleidox.crystalshard.internal.items.channel.TextChannelInternal;
import de.kaleidox.crystalshard.internal.items.message.MessageInternal;
import de.kaleidox.crystalshard.internal.util.RoleContainer;
import de.kaleidox.crystalshard.main.handling.editevent.EditTrait;
import de.kaleidox.crystalshard.main.handling.listener.message.generic.MessageEditListener;
import de.kaleidox.crystalshard.main.items.channel.ServerChannel;
import de.kaleidox.crystalshard.main.items.channel.TextChannel;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.items.message.embed.SentEmbed;
import de.kaleidox.crystalshard.main.items.role.Role;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.user.User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class MESSAGE_UPDATE extends HandlerBase {
    // Override Methods
    @Override
    public void handle(DiscordInternal discord, JsonNode data) {
        MessageInternal message = (MessageInternal) discord.getMessageCache()
                .getOrCreate(discord, data);
        TextChannel channel = message.getChannel();
        Server server = channel.toServerChannel()
                .map(ServerChannel::getServer)
                .orElse(null);
        User user = message.getAuthorAsUser()
                .orElse(null);
        Collection<Role> roles = (user != null ? user.getRoles(server) : Collections.emptyList());

        List<SentEmbed> embeds = message.getEmbeds();
        SentEmbed prevEmbed = (embeds.isEmpty() ? null : embeds.get(0));
        String prevContent = message.getContent();
        Set<EditTrait<Message>> traits = message.updateData(data);
        if (message.isPinned()) {
            ((TextChannelInternal) channel).updatePinned(message);
        }

        MessageEditEventInternal event = new MessageEditEventInternal(discord, message, traits, prevContent, prevEmbed);

        collectListeners(MessageEditListener.class,
                discord,
                server,
                channel,
                user,
                new RoleContainer(roles),
                message).forEach(listener -> discord.getThreadPool()
                .execute(() -> listener.onMessageEdit(event)));
    }
}
