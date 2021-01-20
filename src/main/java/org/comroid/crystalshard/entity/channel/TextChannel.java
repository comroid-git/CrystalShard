package org.comroid.crystalshard.entity.channel;

import org.comroid.crystalshard.entity.SnowflakeCache;
import org.comroid.crystalshard.entity.message.Message;
import org.comroid.crystalshard.model.MessageTarget;
import org.comroid.util.StandardValueType;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.Contract;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public interface TextChannel extends Channel, MessageTarget {
    GroupBind<TextChannel> BASETYPE
            = Channel.BASETYPE.subGroup("text-channel");
    VarBind<TextChannel, Long, Message, Message> LAST_MESSAGE
            = BASETYPE.createBind("last_message_id")
            .extractAs(StandardValueType.LONG)
            .andResolveRef((channel, id) -> channel.requireFromContext(SnowflakeCache.class).getMessage(id))
            .onceEach()
            .build();
    VarBind<TextChannel, Integer, Duration, Duration> SLOWMODE_COOLDOWN
            = BASETYPE.createBind("rate_limit_per_user")
            .extractAs(StandardValueType.INTEGER)
            .andRemap(Duration::ofSeconds)
            .onceEach()
            .setRequired()
            .build();

    @Override
    @Contract("-> this")
    default CompletableFuture<TextChannel> getTargetChannel() {
        return CompletableFuture.completedFuture(this);
    }
}
