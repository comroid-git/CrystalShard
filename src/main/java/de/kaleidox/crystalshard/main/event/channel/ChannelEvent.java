package de.kaleidox.crystalshard.main.event.channel;

import de.kaleidox.crystalshard.main.items.channel.Channel;

import java.util.concurrent.CompletableFuture;

public interface ChannelEvent {
    Channel getChannel();

    CompletableFuture<Channel> requestChannel();
}
