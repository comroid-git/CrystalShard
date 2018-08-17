package de.kaleidox.crystalshard.main.items.message;

import de.kaleidox.crystalshard.main.items.Nameable;

public interface MessageApplication extends Nameable {
    long getId();

    String getDescription();

    String getCoverId();

    String getIconId();

}
