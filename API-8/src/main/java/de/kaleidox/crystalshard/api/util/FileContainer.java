package de.kaleidox.crystalshard.api.util;

public interface FileContainer {
    String getFullName();

    interface Containable {
        FileContainer getContainer();
    }
}
