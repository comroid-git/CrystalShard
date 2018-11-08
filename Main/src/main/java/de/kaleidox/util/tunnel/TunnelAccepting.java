package de.kaleidox.util.tunnel;

import de.kaleidox.util.annotations.Nullable;
import java.util.concurrent.CompletableFuture;

public interface TunnelAccepting {
    <B, T, SC extends TunnelAcceptor<B, T>> void tunnelMethod(Tunnel<B, T, SC> tunnel);

    static <C extends TunnelAccepting, B, T, SC extends TunnelAcceptor<B, T>> void startTunneling(
            @Nullable C tunneler, B baseItem, CompletableFuture<T> futureItem, Class<SC> tunnelIntoClass) {
        if (tunneler != null) tunneler.tunnelMethod(new Tunnel<>(baseItem, futureItem, tunnelIntoClass));
    }
}
