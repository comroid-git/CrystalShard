package de.kaleidox.crystalshard.internal.event.message;

import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.event.EventBase;
import de.kaleidox.crystalshard.main.handling.editevent.EditTrait;
import de.kaleidox.crystalshard.main.handling.editevent.enums.MessageEditTrait;
import de.kaleidox.crystalshard.main.handling.event.message.generic.MessageEditEvent;
import de.kaleidox.crystalshard.main.handling.event.types.MessageAttachingEvent;
import de.kaleidox.crystalshard.main.items.channel.Channel;
import de.kaleidox.crystalshard.main.items.channel.ServerChannel;
import de.kaleidox.crystalshard.main.items.channel.TextChannel;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.items.message.embed.SentEmbed;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.user.Author;
import de.kaleidox.crystalshard.main.items.user.AuthorUser;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class MessageEditEventInternal extends EventBase implements MessageEditEvent {
    private final Message message;
    private final TextChannel channel;
    private final String prevContent;
    private final SentEmbed prevEmbed;
    private final long messageId;
    private final Set<EditTrait<Message>> traits;

    public MessageEditEventInternal(DiscordInternal discordInternal,
                                    Message message,
                                    String prevContent,
                                    SentEmbed prevEmbed) {
        super(discordInternal, MessageAttachingEvent.MESSAGE_EDIT);
        this.message = message;
        this.messageId = message.getId();
        this.channel = message.getChannel();
        this.prevContent = prevContent;
        this.prevEmbed = prevEmbed;
        this.traits = new HashSet<>() {{
            if (prevContent != null) add(MessageEditTrait.CONTENT);
            if (prevEmbed != null) add(MessageEditTrait.EMBED);
        }};
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

    @Override
    public Optional<SentEmbed> getPreviousEmbed() {
        return Optional.ofNullable(prevEmbed);
    }

    @Override
    public Set<EditTrait<Message>> getEditTraits() {
        return traits;
    }

    @Override
    public Optional<Message> getPreviousValue() {
        return Optional.empty();
    }
}
