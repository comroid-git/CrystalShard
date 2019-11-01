package de.comroid.crystalshard.api.model.message;

import java.util.Optional;

import de.comroid.crystalshard.api.entity.guild.webhook.Webhook;
import de.comroid.crystalshard.api.entity.message.Message;
import de.comroid.crystalshard.api.entity.user.User;
import de.comroid.crystalshard.util.model.TypeGroup;

public interface MessageAuthor {
    Optional<Message> getLatestMessage();

    default Optional<User> castAuthorToUser() {
        return TypeGroup.cast(this, User.class);
    }

    default Optional<Webhook> castAuthorToWebhook() {
        return TypeGroup.cast(this, Webhook.class);
    }
}
