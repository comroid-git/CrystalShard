package org.comroid.crystalshard.core.gateway;

import java.util.concurrent.CompletableFuture;

import org.comroid.crystalshard.api.event.EventHandler;
import org.comroid.crystalshard.api.model.ApiBound;
import org.comroid.crystalshard.core.gateway.event.GatewayEventBase;

import com.alibaba.fastjson.JSONObject;
import org.jetbrains.annotations.Nullable;

public interface Gateway extends EventHandler<GatewayEventBase>, ApiBound {
    CompletableFuture<Void> sendRequest(OpCode code, @Nullable JSONObject payload);
}

