package de.comroid.crystalshard.impl;

import java.util.concurrent.CompletableFuture;

import de.comroid.crystalshard.CrystalShard;
import de.comroid.crystalshard.abstraction.handling.AbstractEventHandler;
import de.comroid.crystalshard.adapter.Adapter;
import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.api.event.multipart.APIEvent;
import de.comroid.crystalshard.api.model.user.Yourself;
import de.comroid.crystalshard.core.cache.CacheManager;
import de.comroid.crystalshard.core.concurrent.ThreadPool;
import de.comroid.crystalshard.core.gateway.Gateway;
import de.comroid.crystalshard.core.gateway.OpCode;
import de.comroid.crystalshard.core.gateway.event.READY;
import de.comroid.crystalshard.core.rest.Ratelimiter;
import de.comroid.crystalshard.core.gateway.GatewayImpl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.flogger.FluentLogger;

public class DiscordImpl extends AbstractEventHandler<APIEvent> implements Discord {
    private final static FluentLogger log = FluentLogger.forEnclosingClass();
    
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
        super();
        
        this.token = token;
        this.shardId = shardId;
        
        this.cacheManager = Adapter.require(CacheManager.class, this);
        
        this.commonThreadPool = Adapter.require(ThreadPool.class, this, "Common", Integer.MAX_VALUE);
        this.gatewayThreadPool = Adapter.require(ThreadPool.class, this, "Gateway", Integer.MAX_VALUE);
        this.listenerThreadPool = Adapter.require(ThreadPool.class, this, "Listener", Integer.MAX_VALUE);
        this.ratelimiterThreadPool = Adapter.require(ThreadPool.class, this, "Ratelimiter", Integer.MAX_VALUE);
        
        this.ratelimiter = Adapter.require(Ratelimiter.class, this, ratelimiterThreadPool);
        this.gateway = Adapter.require(Gateway.class, this, gatewayThreadPool);
        
        this.yourself = ((GatewayImpl) gateway).helloFuture
                .thenCompose(nil -> {
                    final JSONObject identify = new JSONObject();
                    
                    identify.put("token", token);
                    identify.put("large_threshold", 250);
                    identify.put("shard", shardId);
                            
                    final JSONObject properties = new JSONObject();
                    properties.put("$os", System.getenv("os.name"));
                    properties.put("$browser", "CrystalShard " + CrystalShard.VERSION);
                    properties.put("$device", "CrystalShard " + CrystalShard.VERSION);
                    identify.put("properties", properties);

                    final CompletableFuture<READY> readyFuture = gateway.listenTo(READY.class).onlyOnce();
                    gateway.sendRequest(OpCode.IDENTIFY, identify);
                    
                    return readyFuture;
                })
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
}
