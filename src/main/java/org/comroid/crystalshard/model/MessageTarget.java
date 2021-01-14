package org.comroid.crystalshard.model;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.Bot;
import org.comroid.crystalshard.entity.channel.PrivateTextChannel;
import org.comroid.crystalshard.entity.channel.TextChannel;
import org.comroid.crystalshard.entity.message.Message;
import org.comroid.crystalshard.rest.Endpoint;
import org.comroid.restless.REST;
import org.comroid.restless.body.BodyBuilderType;

import java.util.concurrent.CompletableFuture;

public interface MessageTarget extends ContextualProvider {
    CompletableFuture<? extends TextChannel> getTargetChannel();

    default CompletableFuture<Message> sendText(String text) {
        return getTargetChannel().thenCompose(tc -> requireFromContext(Bot.class).newRequest(
                REST.Method.POST,
                Endpoint.SEND_MESSAGE.complete(tc.getID()),
                Message.TYPE,
                BodyBuilderType.OBJECT,
                obj -> obj.put("content", text)
        ));
    }
}
