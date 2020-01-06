package org.comroid.crystalshard.util.interfaces;

import java.io.IOException;

/**
 * Defines an object to require initialization.
 * During initialization, the class may throw IOExceptions.
 */
public interface Initializable {
    /**
     * The method to be called to initialize the object.
     *
     * @throws IOException Exceptions that happen during IO operations on initialization.
     */
    void init() throws IOException;
}
