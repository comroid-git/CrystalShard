package de.kaleidox.crystalshard.main.items.user;

import de.kaleidox.crystalshard.main.items.DiscordItem;
import de.kaleidox.crystalshard.main.items.Nameable;
import de.kaleidox.crystalshard.main.util.Castable;

import java.net.URL;
import java.util.Optional;

/**
 * This interface represents a Webhook.
 */
public interface Webhook extends DiscordItem, Nameable, Castable<Webhook> {
    Optional<URL> getAvatarUrl();
    
    Optional<AuthorWebhook> toAuthorWebhook();
}
