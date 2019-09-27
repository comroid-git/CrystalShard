package de.kaleidox.crystalshard.api.model.message;

import java.util.Optional;

import de.kaleidox.crystalshard.api.entity.message.Message;
import de.kaleidox.crystalshard.api.entity.user.User;
import de.kaleidox.crystalshard.api.entity.guild.webhook.Webhook;
import de.kaleidox.crystalshard.util.model.TypeGroup;

public interface MessageAuthor {
    Optional<Message> getLatestMessage();

    default Optional<User> castAuthorToUser() {
        return TypeGroup.cast(this, User.class);
    }

    default Optional<Webhook> castAuthorToWebhook() {
        return TypeGroup.cast(this, Webhook.class);
    }
}
