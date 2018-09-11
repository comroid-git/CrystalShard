package de.kaleidox.crystalshard.internal.handling.event.message.generic;

import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.EventBase;
import de.kaleidox.crystalshard.main.handling.event.message.generic.MessageDeleteEvent;
import de.kaleidox.crystalshard.main.items.channel.Channel;
import de.kaleidox.crystalshard.main.items.channel.TextChannel;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.items.user.User;

import java.util.Optional;

public class MessageDeleteEventInternal extends EventBase implements MessageDeleteEvent {
    private final Message message;
    private final TextChannel channel;
    private final long messageId;
    private final User deleter;

    public MessageDeleteEventInternal(DiscordInternal discordInternal,
                                      Message message, User deleter) {
        super(discordInternal);
        this.message = message;
        this.messageId = message.getId();
        this.channel = message.getChannel();
        this.deleter = deleter;
    }

    @Override
    public Optional<User> getDeleter() {
        return Optional.ofNullable(deleter);
    }

    @Override
    public Message getMessage() {
        return message;
    }

    @Override
    public Channel getChannel() {
        return channel;
    }
}
