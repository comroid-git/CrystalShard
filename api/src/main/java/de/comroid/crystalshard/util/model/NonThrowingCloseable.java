package de.comroid.crystalshard.util.model;

import java.io.Closeable;

public interface NonThrowingCloseable extends Closeable {
    @Override
    void close();
}
