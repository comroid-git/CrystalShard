package de.kaleidox.crystalshard.internal.items.user;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.items.message.MessageInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.items.user.Author;
import de.kaleidox.crystalshard.main.items.user.AuthorUser;
import de.kaleidox.crystalshard.main.items.user.AuthorWebhook;
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
    public Optional<AuthorWebhook> toAuthorWebhook() {
        return Optional.of(this);
    }
    
    @Override
    public boolean isYourself() {
        return equals(getDiscord().getSelf()); // todo Can we assert NO? (Check Webhook functionality)
    }
}
