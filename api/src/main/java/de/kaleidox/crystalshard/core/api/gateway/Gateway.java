package de.kaleidox.crystalshard.core.api.gateway;

import java.util.concurrent.CompletableFuture;

import de.kaleidox.crystalshard.api.listener.ListenerAttachable;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListener;

public interface Gateway extends ListenerAttachable<GatewayListener> {
    CompletableFuture<Void> sendRequest(OpCode code, String payload);
}

