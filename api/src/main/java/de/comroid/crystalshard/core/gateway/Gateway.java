package de.comroid.crystalshard.core.gateway;

import java.util.concurrent.CompletableFuture;

import de.comroid.crystalshard.api.event.EventHandler;
import de.comroid.crystalshard.api.model.ApiBound;
import de.comroid.crystalshard.core.gateway.event.GatewayEventBase;

import com.alibaba.fastjson.JSONObject;
import org.jetbrains.annotations.Nullable;

public interface Gateway extends EventHandler<GatewayEventBase>, ApiBound {
    CompletableFuture<Void> sendRequest(OpCode code, @Nullable JSONObject payload);
}

