package de.kaleidox.crystalshard.internal.items.user;

import com.fasterxml.jackson.databind.JsonNode;

import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.entity.message.Message;
import de.kaleidox.crystalshard.api.entity.user.Author;
import de.kaleidox.crystalshard.api.entity.user.AuthorUser;
import de.kaleidox.crystalshard.api.entity.user.AuthorWebhook;
import de.kaleidox.crystalshard.internal.items.message.MessageInternal;

import java.util.Optional;

public class AuthorWebhookInternal extends WebhookInternal implements Author, AuthorWebhook {
    private final Message message;

    public AuthorWebhookInternal(Discord discord, MessageInternal message, JsonNode data) {
        super(discord, data);
        this.message = message;
    }

    // Override Methods
    @Override
    public Message getMessage() {
        return message;
    }

    @Override
    public Optional<AuthorUser> toAuthorUser() {
        return Optional.empty();
    }

    @Override
    public boolean isYourself() {
        return equals(getDiscord().getSelf()); // todo Can we assert NO? (Check Webhook functionality)
    }

    @Override
    public Optional<AuthorWebhook> toAuthorWebhook() {
        return Optional.of(this);
    }
}
