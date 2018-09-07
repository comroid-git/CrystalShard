package de.kaleidox.crystalshard.internal.event.message;

import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.event.EventBase;
import de.kaleidox.crystalshard.main.event.message.MessageEditEvent;
import de.kaleidox.crystalshard.main.event.types.MessageAttachingEvent;
import de.kaleidox.crystalshard.main.items.channel.Channel;
import de.kaleidox.crystalshard.main.items.channel.ServerChannel;
import de.kaleidox.crystalshard.main.items.channel.TextChannel;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.user.Author;
import de.kaleidox.crystalshard.main.items.user.AuthorUser;

import java.util.Optional;

public class MessageEditEventInternal extends EventBase implements MessageEditEvent {
    private final Message message;
    private final TextChannel channel;
    private final String prevContent;
    private final long messageId;

    public MessageEditEventInternal(DiscordInternal discordInternal, Message message, String prevContent) {
        super(discordInternal, MessageAttachingEvent.MESSAGE_EDIT);
        this.message = message;
        this.messageId = message.getId();
        this.channel = message.getChannel();
        this.prevContent = prevContent;
    }

    @Override
    public Message getMessage() {
        return message;
    }

    public long getMessageId() {
        return messageId;
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

    @Override
    public Optional<String> getPreviousContent() {
        return Optional.ofNullable(prevContent);
    }
}
