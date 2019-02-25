package de.kaleidox.crystalshard.internal.handling.event.message.generic;

import de.kaleidox.crystalshard.api.entity.channel.Channel;
import de.kaleidox.crystalshard.api.entity.channel.ServerChannel;
import de.kaleidox.crystalshard.api.entity.channel.TextChannel;
import de.kaleidox.crystalshard.api.entity.message.Message;
import de.kaleidox.crystalshard.api.entity.server.Server;
import de.kaleidox.crystalshard.api.entity.user.Author;
import de.kaleidox.crystalshard.api.entity.user.AuthorUser;
import de.kaleidox.crystalshard.api.handling.event.message.generic.MessageCreateEvent;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.EventBase;

import java.util.Optional;

public class MessageCreateEventInternal extends EventBase implements MessageCreateEvent {
    private final Message message;
    private final TextChannel channel;
    private final long messageId;

    public MessageCreateEventInternal(DiscordInternal discordInternal, Message message) {
        super(discordInternal);
        this.message = message;
        this.messageId = message.getId();
        this.channel = message.getChannel();
    }

    // Override Methods
    @Override
    public Message getMessage() {
        return message;
    }

    @Override
    public Channel getChannel() {
        return channel;
    }

    @Override
    public Author getMessageAuthor() {
        return message.getAuthor();
    }

    @Override
    public Optional<AuthorUser> getMessageAuthorUser() {
        return message.getAuthorAsUser();
    }

    @Override
    public String getMessageContent() {
        return message.getContent();
    }

    @Override
    public Optional<Server> getServer() {
        return message.getChannel()
                .toServerChannel()
                .map(ServerChannel::getServer);
    }
}
