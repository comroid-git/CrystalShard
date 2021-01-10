package org.comroid.crystalshard.model;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.Bot;
import org.comroid.crystalshard.entity.channel.TextChannel;
import org.comroid.crystalshard.entity.message.Message;
import org.comroid.crystalshard.rest.Endpoint;
import org.comroid.restless.REST;

import java.util.concurrent.CompletableFuture;

public interface MessageTarget extends ContextualProvider {
    TextChannel getTextChannel();

    default CompletableFuture<Message> sendText(String text) {
        return requireFromContext(Bot.class)
                .newRequest(REST.Method.POST, Endpoint.SEND_MESSAGE); // todo
    }
}
