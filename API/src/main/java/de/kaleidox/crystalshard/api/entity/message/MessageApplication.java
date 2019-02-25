package de.kaleidox.crystalshard.api.entity.message;

import de.kaleidox.crystalshard.api.entity.Nameable;

public interface MessageApplication extends Nameable {
    long getId();

    String getDescription();

    String getCoverId();

    String getIconId();
}
