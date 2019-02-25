package de.kaleidox.crystalshard.api.entity.user;

import de.kaleidox.crystalshard.api.entity.DiscordItem;
import de.kaleidox.crystalshard.api.entity.Nameable;
import de.kaleidox.crystalshard.api.entity.message.Message;

import java.util.Optional;

/**
 * This interface represents an Author of a message.
 */
public interface Author extends DiscordItem, Nameable {
    /**
     * The message the author has been obtained from.
     *
     * @return The authored message.
     */
    Message getMessage();

    Optional<AuthorUser> toAuthorUser();

    Optional<AuthorWebhook> toAuthorWebhook();

    boolean isYourself();

    /**
     * Returns whether the author of the message was a user.
     *
     * @return Whether the author was a user.
     */
    default boolean isAuthorUser() {
        return this instanceof AuthorUser;
    }

    /**
     * Returns whether the author of the message was a webhook.
     *
     * @return Whether the author was a webhook.
     */
    default boolean isAuthorWebhook() {
        return this instanceof AuthorWebhook;
    }
}
