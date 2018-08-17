package de.kaleidox.crystalshard.main.items.user;

import de.kaleidox.crystalshard.main.items.DiscordItem;
import de.kaleidox.crystalshard.main.items.Nameable;

import java.net.URL;
import java.util.Optional;

/**
 * This interface represents a Webhook.
 */
public interface Webhook extends DiscordItem, Nameable {
    Optional<URL> getAvatarUrl();
}
