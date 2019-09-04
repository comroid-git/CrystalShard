package de.kaleidox.crystalshard.api.entity.message;

// https://discordapp.com/developers/docs/resources/channel#message-object-message-application-structure

import java.net.URL;
import java.util.Optional;

import de.kaleidox.crystalshard.api.entity.Snowflake;

public interface MessageApplication extends Snowflake {
    Optional<URL> getCoverImageURL();

    String getDescription();

    Optional<URL> getIconURL();

    String getName();
}
