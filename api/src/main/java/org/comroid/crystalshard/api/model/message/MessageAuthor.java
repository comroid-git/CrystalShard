package org.comroid.crystalshard.api.model.message;

import java.util.Optional;

import org.comroid.crystalshard.api.entity.guild.webhook.Webhook;
import org.comroid.crystalshard.api.entity.message.Message;
import org.comroid.crystalshard.api.entity.user.User;
import org.comroid.crystalshard.util.model.TypeGroup;

public interface MessageAuthor {
    Optional<Message> getLatestMessage();

    default Optional<User> castAuthorToUser() {
        return TypeGroup.cast(this, User.class);
    }

    default Optional<Webhook> castAuthorToWebhook() {
        return TypeGroup.cast(this, Webhook.class);
    }

    default boolean isBotOwner() {
        return castAuthorToUser().map(User::isBotOwner).orElse(false);
    }
}
