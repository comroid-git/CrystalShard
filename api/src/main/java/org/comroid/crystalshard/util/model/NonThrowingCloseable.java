package org.comroid.crystalshard.util.model;

import java.io.Closeable;

public interface NonThrowingCloseable extends Closeable {
    @Override
    void close();
    
    static NonThrowingCloseable combine(NonThrowingCloseable... nonThrowingCloseables) {
        return () -> {
            for (NonThrowingCloseable closeable : nonThrowingCloseables) closeable.close();
        };
    }
}
