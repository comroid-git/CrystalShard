package de.kaleidox.crystalshard.core.api.gateway;

import java.util.concurrent.CompletableFuture;

import de.kaleidox.crystalshard.api.listener.model.ListenerAttachable;
import de.kaleidox.crystalshard.api.model.ApiBound;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListener;

public interface Gateway extends ListenerAttachable<GatewayListener>, ApiBound {
    CompletableFuture<Void> sendRequest(OpCode code, String payload);
}

