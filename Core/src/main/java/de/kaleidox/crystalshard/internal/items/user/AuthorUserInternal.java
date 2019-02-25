package de.kaleidox.crystalshard.internal.items.user;

import com.fasterxml.jackson.databind.JsonNode;

import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.entity.message.Message;
import de.kaleidox.crystalshard.api.entity.user.Author;
import de.kaleidox.crystalshard.api.entity.user.AuthorUser;
import de.kaleidox.crystalshard.api.entity.user.AuthorWebhook;
import de.kaleidox.crystalshard.internal.items.message.MessageInternal;

import java.util.Optional;

public class AuthorUserInternal extends UserInternal implements Author, AuthorUser {
    private final MessageInternal message;

    public AuthorUserInternal(Discord discord, MessageInternal messageParent, JsonNode data) {
        super(discord, data);
        this.message = messageParent;
    }

    // Override Methods
    @Override
    public Message getMessage() {
        return message;
    }

    @Override
    public Optional<AuthorUser> toAuthorUser() {
        return Optional.of(this);
    }

    @Override
    public Optional<AuthorWebhook> toAuthorWebhook() {
        return Optional.empty();
    }
}
