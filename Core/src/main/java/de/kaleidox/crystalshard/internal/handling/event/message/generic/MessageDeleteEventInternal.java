package de.kaleidox.crystalshard.internal.handling.event.message.generic;

import de.kaleidox.crystalshard.api.entity.channel.Channel;
import de.kaleidox.crystalshard.api.entity.channel.TextChannel;
import de.kaleidox.crystalshard.api.entity.message.Message;
import de.kaleidox.crystalshard.api.handling.event.message.generic.MessageDeleteEvent;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.EventBase;

public class MessageDeleteEventInternal extends EventBase implements MessageDeleteEvent {
    private final Message message;
    private final TextChannel channel;
    private final long messageId;

    public MessageDeleteEventInternal(DiscordInternal discordInternal, Message message) {
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
}
