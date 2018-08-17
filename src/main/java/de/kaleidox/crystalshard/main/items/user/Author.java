package de.kaleidox.crystalshard.main.items.user;

import de.kaleidox.crystalshard.main.items.DiscordItem;
import de.kaleidox.crystalshard.main.items.Nameable;
import de.kaleidox.crystalshard.main.items.message.Message;

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

    /**
     * Returns the Author as an AuthorUser.
     * Returns empty if the author is a webhook.
     *
     * @return An Optional that may contain this author as a AuthorUser.
     */
    default Optional<AuthorUser> asAuthorUser() {
        if (this instanceof AuthorUser) {
            return Optional.of((AuthorUser) this);
        }
        return Optional.empty();
    }

    /**
     * Returns the Author as an AuthorWebhook.
     * Returns empty if the author is a user.
     *
     * @return An Optional that may contain this author as a AuthorWebhook.
     */
    default Optional<AuthorWebhook> asAuthorWebhook() {
        if (this instanceof AuthorWebhook) {
            return Optional.of((AuthorWebhook) this);
        }
        return Optional.empty();
    }
}
