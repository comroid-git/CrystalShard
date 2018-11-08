package de.kaleidox.util.tunnel;

import java.util.concurrent.CompletableFuture;

public interface TunnelAcceptor<B, T> {
    void acceptBase(B base, CompletableFuture<T> futureResolver);
}
