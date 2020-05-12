package org.comroid.crystalshard.model.message;

import org.comroid.crystalshard.entity.channel.TextChannel;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.ApiStatus.Internal;

import java.util.concurrent.CompletableFuture;

@Internal
public interface MessageOperator {
    CompletableFuture<TextChannel> getCorrespondenceChannel();
}
