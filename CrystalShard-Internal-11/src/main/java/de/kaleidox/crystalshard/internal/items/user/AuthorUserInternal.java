package de.kaleidox.crystalshard.internal.items.user;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.items.message.MessageInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.items.user.Author;
import de.kaleidox.crystalshard.main.items.user.AuthorUser;
import de.kaleidox.crystalshard.main.items.user.AuthorWebhook;
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
    public Optional<AuthorWebhook> toAuthorWebhook() {
        return Optional.empty();
    }
    
    @Override
    public Optional<AuthorUser> toAuthorUser() {
        return Optional.of(this);
    }
}
