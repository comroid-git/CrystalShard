package de.kaleidox.crystalshard.internal.handling.event.message.generic;

import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.EventBase;
import de.kaleidox.crystalshard.main.handling.event.message.generic.MessageCreateEvent;
import de.kaleidox.crystalshard.main.items.channel.Channel;
import de.kaleidox.crystalshard.main.items.channel.ServerChannel;
import de.kaleidox.crystalshard.main.items.channel.TextChannel;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.user.Author;
import de.kaleidox.crystalshard.main.items.user.AuthorUser;
import java.util.Optional;

public class MessageCreateEventInternal extends EventBase implements MessageCreateEvent {
    private final Message     message;
    private final TextChannel channel;
    private final long        messageId;
    
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
