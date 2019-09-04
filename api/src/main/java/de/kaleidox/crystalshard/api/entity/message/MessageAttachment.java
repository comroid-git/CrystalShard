package de.kaleidox.crystalshard.api.entity.message;

import java.net.URL;
import java.util.OptionalInt;

import de.kaleidox.crystalshard.api.entity.Snowflake;

public interface MessageAttachment extends Snowflake {
    String getFilename();

    int getFilesize();

    URL getURL();

    URL getProxyURL();

    OptionalInt getHeight();

    OptionalInt getWidth();
}
