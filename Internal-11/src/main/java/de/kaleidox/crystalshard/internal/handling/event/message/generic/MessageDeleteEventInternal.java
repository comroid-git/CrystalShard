package de.kaleidox.crystalshard.internal.handling.event.message.generic;

import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.EventBase;
import de.kaleidox.crystalshard.main.handling.event.message.generic.MessageDeleteEvent;
import de.kaleidox.crystalshard.main.items.channel.Channel;
import de.kaleidox.crystalshard.main.items.channel.TextChannel;
import de.kaleidox.crystalshard.main.items.message.Message;

public class MessageDeleteEventInternal extends EventBase implements MessageDeleteEvent {
    private final Message     message;
    private final TextChannel channel;
    private final long        messageId;
    
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
