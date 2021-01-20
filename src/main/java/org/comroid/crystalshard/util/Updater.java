package org.comroid.crystalshard.util;

import org.comroid.api.Provider;

import java.util.concurrent.CompletableFuture;

public interface Updater<R> extends Provider<R> {
    @Override
    default CompletableFuture<R> get() {
        return update();
    }

    CompletableFuture<R> update();
}
