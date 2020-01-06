package org.comroid.crystalshard.impl.model.channel;

import java.util.concurrent.CompletableFuture;

import org.comroid.crystalshard.api.entity.channel.TextChannel;
import org.comroid.crystalshard.api.model.channel.MessagePrintStream;

public class MessagePrintStreamImpl extends MessagePrintStream {
    public MessagePrintStreamImpl(TextChannel channel) {
        super(channel, new ChannelOutputStream(channel));
    }

    public MessagePrintStreamImpl(CompletableFuture<TextChannel> future) {
        //noinspection ConstantConditions
        super(new ChannelOutputStream(null));

        future.thenAcceptAsync(chl -> {
            super.channel = chl;
            ((ChannelOutputStream) this.out).textChannel = chl;
        });
    }
}
