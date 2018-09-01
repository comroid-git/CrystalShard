package de.kaleidox.crystalshard.main.event.channel;

import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.main.event.EventBase;
import de.kaleidox.crystalshard.main.event.MessageEvent;
import de.kaleidox.crystalshard.main.items.channel.Channel;
import de.kaleidox.crystalshard.main.items.channel.PrivateChannel;
import de.kaleidox.crystalshard.main.items.channel.PrivateTextChannel;
import de.kaleidox.crystalshard.main.items.channel.ServerTextChannel;
import de.kaleidox.crystalshard.main.items.channel.TextChannel;
import de.kaleidox.crystalshard.main.items.message.Message;

import java.util.Optional;

public class MessageCreateEvent extends EventBase implements MessageEvent, ChannelEvent {
    private final Message message;
    private final TextChannel channel;

    public MessageCreateEvent(DiscordInternal discordInternal,
                              Message message) {
        super(discordInternal);
        this.message = message;
        this.channel = message.getChannel();
    }

    @Override
    public Message getMessage() {
        return message;
    }

    @Override
    public Channel getChannel() {
        return channel;
    }

    public Optional<ServerTextChannel> getServerTextChannel() {
        return channel.toServerTextChannel();
    }

    public Optional<PrivateTextChannel> getPrivateTextChannel() {
        return channel.toPrivateTextChannel();
    }
}
