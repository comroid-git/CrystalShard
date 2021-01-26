package org.comroid.crystalshard.entity.channel;

import org.comroid.crystalshard.entity.message.Message;
import org.comroid.crystalshard.model.message.MessageBuilder;
import org.comroid.crystalshard.model.message.MessageTarget;
import org.comroid.crystalshard.rest.Endpoint;
import org.comroid.restless.REST;
import org.comroid.restless.body.BodyBuilderType;
import org.comroid.util.StandardValueType;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public interface TextChannel extends Channel, MessageTarget {
    GroupBind<TextChannel> BASETYPE
            = Channel.BASETYPE.subGroup("text-channel");
    VarBind<TextChannel, Long, Message, Message> LAST_MESSAGE
            = BASETYPE.createBind("last_message_id")
            .extractAs(StandardValueType.LONG)
            .andResolveRef((channel, id) -> channel.getCache().getMessage(id))
            .build();
    VarBind<TextChannel, Integer, Duration, Duration> SLOWMODE_COOLDOWN
            = BASETYPE.createBind("rate_limit_per_user")
            .extractAs(StandardValueType.INTEGER)
            .andRemap(Duration::ofSeconds)
            .build();

    @Override
    default CompletableFuture<Message> executeMessage(MessageBuilder builder) {
        return getBot().newRequest(
                REST.Method.POST,
                Endpoint.SEND_MESSAGE,
                BodyBuilderType.OBJECT,
                builder,
                Message.TYPE
        );
    }
}
