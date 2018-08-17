package de.kaleidox.crystalshard.main.items.user;

import de.kaleidox.crystalshard.main.items.DiscordItem;
import de.kaleidox.crystalshard.main.items.Nameable;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.util.Castable;

import java.util.Optional;

/**
 * This interface represents an Author of a message.
 */
public interface Author extends DiscordItem, Nameable, Castable<Author> {
    /**
     * The message the author has been obtained from.
     *
     * @return The authored message.
     */
    Message getMessage();

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

    default Optional<AuthorUser> toAuthorUser() {
        return castTo(AuthorUser.class);
    }

    default Optional<AuthorWebhook> toAuthorWebhook() {
        return castTo(AuthorWebhook.class);
    }
}
