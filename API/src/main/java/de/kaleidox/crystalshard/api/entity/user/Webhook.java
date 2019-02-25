package de.kaleidox.crystalshard.api.entity.user;

import de.kaleidox.crystalshard.api.entity.DiscordItem;
import de.kaleidox.crystalshard.api.entity.Nameable;
import de.kaleidox.crystalshard.api.util.Castable;

import java.net.URL;
import java.util.Optional;

/**
 * This interface represents a Webhook.
 */
public interface Webhook extends DiscordItem, Nameable, Castable<Webhook> {
    Optional<URL> getAvatarUrl();

    Optional<AuthorWebhook> toAuthorWebhook();
}
