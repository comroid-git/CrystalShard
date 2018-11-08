package de.kaleidox.util.tunnel;

public interface Tunnelable<F> {
    void handleTunnel(F future);
}
