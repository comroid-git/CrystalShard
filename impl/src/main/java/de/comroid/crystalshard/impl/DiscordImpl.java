package de.comroid.crystalshard.impl;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.comroid.crystalshard.CrystalShard;
import de.comroid.crystalshard.adapter.Adapter;
import de.comroid.crystalshard.api.event.DiscordEvent;
import de.comroid.crystalshard.api.event.model.Event;
import de.comroid.crystalshard.api.listener.AttachableTo;
import de.comroid.crystalshard.api.listener.model.Listener;
import de.comroid.crystalshard.api.listener.model.ListenerManager;
import de.comroid.crystalshard.api.model.user.Yourself;
import de.comroid.crystalshard.core.api.cache.CacheManager;
import de.comroid.crystalshard.core.api.concurrent.ThreadPool;
import de.comroid.crystalshard.core.api.gateway.Gateway;
import de.comroid.crystalshard.core.api.gateway.OpCode;
import de.comroid.crystalshard.core.api.gateway.event.READY;
import de.comroid.crystalshard.core.api.rest.Ratelimiter;
import de.comroid.crystalshard.core.gateway.GatewayImpl;
import de.comroid.crystalshard.util.model.NStream;

public class DiscordImpl implements de.comroid.crystalshard.api.Discord {
    private final String token;
    private final int shardId;

    private final CacheManager cacheManager;
    private final ThreadPool commonThreadPool;
    private final ThreadPool gatewayThreadPool;
    private final ThreadPool listenerThreadPool;
    private final ThreadPool ratelimiterThreadPool;
    private final Ratelimiter ratelimiter;
    private final Gateway gateway;
    private final Yourself yourself;
    
    private String sessionId;

    public DiscordImpl(String token, int shardId) {
        this.token = token;
        this.shardId = shardId;
        
        this.cacheManager = Adapter.require(CacheManager.class, this);
        
        this.commonThreadPool = Adapter.require(ThreadPool.class, this, "Common");
        this.gatewayThreadPool = Adapter.require(ThreadPool.class, this, "Gateway");
        this.listenerThreadPool = Adapter.require(ThreadPool.class, this, "Listener");
        this.ratelimiterThreadPool = Adapter.require(ThreadPool.class, this, "Ratelimiter");
        
        this.ratelimiter = Adapter.require(Ratelimiter.class, this, ratelimiterThreadPool);
        this.gateway = Adapter.require(Gateway.class, this, gatewayThreadPool);
        
        this.yourself = ((GatewayImpl) gateway).helloFuture
                .thenCompose(nil -> {
                    final ObjectNode identify = JsonNodeFactory.instance.objectNode();
                    
                    identify.put("token", token);
                    identify.put("large_threshold", 250);
                    identify.put("shard", shardId);
                            
                    final ObjectNode properties = identify.putObject("properties");
                    properties.put("$os", System.getenv("os.name"));
                    properties.put("$browser", "CrystalShard " + CrystalShard.VERSION);
                    properties.put("$device", "CrystalShard " + CrystalShard.VERSION);

                    final CompletableFuture<EventPair<READY, ListenerManager<Listener<? extends READY>>>> readyFuture = gateway.listenOnceTo(READY.class);
                    gateway.sendRequest(OpCode.IDENTIFY, identify);
                    
                    return readyFuture;
                })
                .thenApply(EventPair::getEvent)
                .thenApply(event -> {
                    this.sessionId = event.getSessionID();
                    
                    return event.getYourself();
                })
                .join();
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public int getShardID() {
        return shardId;
    }

    @Override
    public ThreadPool getCommonThreadPool() {
        return commonThreadPool;
    }

    @Override
    public ThreadPool getGatewayThreadPool() {
        return gatewayThreadPool;
    }

    @Override
    public ThreadPool getListenerThreadPool() {
        return listenerThreadPool;
    }

    @Override
    public ThreadPool getRatelimiterThreadPool() {
        return ratelimiterThreadPool;
    }

    @Override
    public Ratelimiter getRatelimiter() {
        return ratelimiter;
    }

    @Override
    public Gateway getGateway() {
        return gateway;
    }

    @Override
    public CacheManager getCacheManager() {
        return cacheManager;
    }

    @Override
    public Yourself getYourself() {
        return yourself;
    }

    @Override
    public <TL extends AttachableTo.Discord<? extends DiscordEvent>> ListenerManager<TL> attachListener(TL listener) {
        return null;
    }

    @Override
    public <TL extends AttachableTo.Discord<? extends DiscordEvent>> boolean detachListener(TL listener) {
        return false;
    }

    @Override
    public Collection<ListenerManager<? extends AttachableTo.Discord<? extends DiscordEvent>>> getAttachedListenerManagers() {
        return null;
    }

    @Override
    public <FE extends Event> CompletableFuture<EventPair<FE, ListenerManager<Listener<? extends FE>>>> listenOnceTo(Class<FE> forEvent) {
        return null;
    }

    @Override
    public <FE extends Event> NStream<EventPair<FE, ListenerManager<Listener<? extends FE>>>> listenInStream(Class<FE> forEvent) {
        return null;
    }
}
