package de.kaleidox.crystalshard.internal.items.message;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.items.user.WebhookInternal;
import de.kaleidox.crystalshard.main.items.user.Author;
import de.kaleidox.crystalshard.main.items.user.AuthorWebhook;
import de.kaleidox.crystalshard.main.items.message.Message;

public class AuthorWebhookInternal extends WebhookInternal implements Author, AuthorWebhook {
    private final Message message;

    public AuthorWebhookInternal(MessageInternal message, JsonNode data) {
        super(data);
        this.message = message;
    }

    @Override
    public Message getMessage() {
        return message;
    }
}
