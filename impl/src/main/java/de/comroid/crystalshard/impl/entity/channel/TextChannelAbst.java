package de.comroid.crystalshard.impl.entity.channel;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.JsonNode;
import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.api.entity.channel.TextChannel;
import de.comroid.crystalshard.api.entity.message.Message;
import de.comroid.crystalshard.api.model.channel.MessagePrintStream;

public class TextChannelAbst<Self extends TextChannelAbst<Self>> extends ChannelAbst<Self> implements TextChannel {
    protected TextChannelAbst(Discord api, JsonNode data) {
        super(api, data);
    }
    
    // todo

    @Override public CompletableFuture<Collection<Message>> requestPinnedMessages() {
        return null;
    }

    @Override public Collection<Message> getMessages(int limit) {
        return null;
    }

    @Override public Stream<Message> getLatestMessagesAsStream() {
        return null;
    }

    @Override public Message.Composer composeMessage() {
        return null;
    }

    @Override public MessagePrintStream openPrintStream() {
        return null;
    }
}
