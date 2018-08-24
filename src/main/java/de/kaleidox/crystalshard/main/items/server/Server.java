package de.kaleidox.crystalshard.main.items.server;

import de.kaleidox.crystalshard.main.items.DiscordItem;
import de.kaleidox.crystalshard.main.items.Nameable;

import java.net.URL;
import java.util.Optional;

public interface Server extends DiscordItem, Nameable {
    Optional<URL> getIconUrl();

    Optional<URL> getSplashUrl();


}
