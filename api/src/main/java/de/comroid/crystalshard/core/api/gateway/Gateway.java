package de.comroid.crystalshard.core.api.gateway;

import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.databind.JsonNode;
import de.comroid.crystalshard.api.listener.model.ListenerAttachable;
import de.comroid.crystalshard.api.model.ApiBound;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListener;

public interface Gateway extends ListenerAttachable<GatewayListener>, ApiBound {
    CompletableFuture<Void> sendRequest(OpCode code, JsonNode payload);
}

