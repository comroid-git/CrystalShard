package de.kaleidox.crystalshard.internal.items.message;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.items.user.UserInternal;
import de.kaleidox.crystalshard.main.items.user.Author;
import de.kaleidox.crystalshard.main.items.user.AuthorUser;
import de.kaleidox.crystalshard.main.items.message.Message;

public class AuthorUserInternal extends UserInternal implements Author, AuthorUser {
    private final MessageInternal message;

    public AuthorUserInternal(MessageInternal messageParent, JsonNode data) {
        super(data);
        this.message = messageParent;
    }

    @Override
    public Message getMessage() {
        return message;
    }
}
