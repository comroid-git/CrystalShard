package de.kaleidox.crystalshard.internal.items;

import de.kaleidox.crystalshard.internal.core.ThreadPool;
import de.kaleidox.websocket.WebSocketClient;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.items.user.AccountType;

public class DiscordInternal implements Discord {
    private final ThreadPool pool;
    private final String token;
    private final AccountType type;
    private final WebSocketClient webSocket;

    public DiscordInternal(String token, AccountType type) {
        this.pool = new ThreadPool(this, 50);
        this.token = token;
        this.type = type;
        this.webSocket = new WebSocketClient(this);
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
}
