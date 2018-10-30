package de.kaleidox.crystalshard.internal.items.message;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.items.message.MessageReciever;
import de.kaleidox.crystalshard.main.items.message.Sendable;
import de.kaleidox.crystalshard.main.items.message.embed.Embed;
import de.kaleidox.crystalshard.main.items.message.embed.EmbedDraft;

import java.util.concurrent.CompletableFuture;

public class SendableInternal implements Sendable {
    private EmbedDraft embed = null;
    private String content = "";

    public SendableInternal(Object... items) {
        for (Object object : items) {
            add(object);
        }
    }

    // Override Methods
    @Override
    public Sendable add(Object object) {
        if (object instanceof Embed && embed == null) {
            embed = ((Embed) object).toEmbedDraft();
        } else if (object instanceof String) {
            //noinspection StringConcatenationInLoop
            content = content + object;
        } else {
            content = object.toString();
        }
        return this;
    }

    @Override
    public Sendable add(String string) {
        content = content + string;
        return this;
    }

    @Override
    public Sendable add(Embed embed) {
        this.embed = embed.toEmbedDraft();
        return null;
    }

    @Override
    public CompletableFuture<Message> send(MessageReciever reciever) {
        return reciever.sendMessage(this);
    }

    public JsonNode toJsonNode(ObjectNode node) {
        return null;
    }
}
