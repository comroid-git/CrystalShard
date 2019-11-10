package de.comroid.crystalshard.core.gateway;

import java.util.concurrent.CompletableFuture;

import de.comroid.crystalshard.api.listener.model.ListenerAttachable;
import de.comroid.crystalshard.api.model.ApiBound;
import de.comroid.crystalshard.core.gateway.listener.GatewayListener;

import com.alibaba.fastjson.JSONObject;
import org.jetbrains.annotations.Nullable;

public interface Gateway extends ListenerAttachable<GatewayListener>, ApiBound {
    CompletableFuture<Void> sendRequest(OpCode code, @Nullable JSONObject payload);
}

