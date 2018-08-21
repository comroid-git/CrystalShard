package de.kaleidox.crystalshard.internal;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.core.Ratelimiting;
import de.kaleidox.crystalshard.internal.core.ThreadPool;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.items.user.AccountType;
import de.kaleidox.logging.Logger;
import de.kaleidox.websocket.WebSocketClient;

public class DiscordInternal implements Discord {
    private final ThreadPool pool;
    private final String token;
    private final AccountType type;
    private final WebSocketClient webSocket;
    private final Ratelimiting ratelimiter;

    public DiscordInternal(String token, AccountType type) {
        this.pool = new ThreadPool(this, 50);
        this.token = token;
        Logger.addBlankedkeyword(token);
        this.type = type;
        this.webSocket = new WebSocketClient(this);
        this.ratelimiter = new Ratelimiting(this);
    }

    public ThreadPool getThreadPool() {
        return pool;
    }

    @Override
    public String getPrefixedToken() {
        return type.getPrefix() + token;
    }

    @Override
    public int getShardId() {
        return 0;
    }

    @Override
    public int getShards() {
        return 1;
    }

    public WebSocketClient getWebSocket() {
        return webSocket;
    }

    public Ratelimiting getRatelimiter() {
        return ratelimiter;
    }

    public void craftServer(JsonNode data) {
    }
}
