package de.kaleidox.crystalshard.main;

import de.kaleidox.crystalshard.main.handling.listener.listener.PreattachableListener;
import de.kaleidox.crystalshard.main.items.user.AccountType;

import java.util.concurrent.CompletableFuture;

public class DiscordLoginTool {
    private String token = "";
    private AccountType type = AccountType.BOT;
    private int shardCount = -1;
    private int shard = -1;

    public DiscordLoginTool() {
    }

    public DiscordLoginTool(String token) {
        this.token = token;
    }

    public DiscordLoginTool setToken(String token) {
        this.token = token;
        return this;
    }

    public DiscordLoginTool setAccountType(AccountType type) {
        this.type = type;
        return this;
    }

    public DiscordLoginTool setShardCount(int count) {
        this.shardCount = count;
        return this;
    }

    public DiscordLoginTool setCurrentShard(int shard) {
        this.shard = shard;
        return this;
    }

    public DiscordLoginTool setRecommendedShardCount() {
        return this; // todo
    }

    public DiscordLoginTool preAttachListener(PreattachableListener listener) {
        return this; // todo
    }

    public CompletableFuture<Discord> login() {
        return null;
    }

    public CompletableFuture<MultiShard> loginMultiShard() {
        return null;
    }
}
