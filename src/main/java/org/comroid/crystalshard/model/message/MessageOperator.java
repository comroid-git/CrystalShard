package org.comroid.crystalshard.model.message;

import org.comroid.crystalshard.entity.channel.TextChannel;

import java.util.concurrent.CompletableFuture;

public interface MessageOperator {
    CompletableFuture<TextChannel> getCorrespondenceChannel();
}
