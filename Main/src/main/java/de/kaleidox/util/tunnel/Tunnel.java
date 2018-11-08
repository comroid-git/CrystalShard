package de.kaleidox.util.tunnel;

import java.util.concurrent.CompletableFuture;

public class Tunnel<B, T, SC> {
    private final B baseItem;
    private final CompletableFuture<T> futureItem;
    private final Class<SC> superClass;

    public Tunnel(B baseItem, CompletableFuture<T> futureItem, Class<SC> tunnelIntoClass) {
        this.baseItem = baseItem;
        this.futureItem = futureItem;
        this.superClass = tunnelIntoClass;
    }

    public B getBaseItem() {
        return baseItem;
    }

    public CompletableFuture<T> getFutureItem() {
        return futureItem;
    }

    public Class<SC> getSuperClass() {
        return superClass;
    }
}
