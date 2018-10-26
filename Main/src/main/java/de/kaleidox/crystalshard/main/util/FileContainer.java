package de.kaleidox.crystalshard.main.util;

public interface FileContainer {
    String getFullName();
    
    interface Containable {
        FileContainer getContainer();
    }
}
