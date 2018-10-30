package de.kaleidox.crystalshard.main;

import de.kaleidox.crystalshard.core.CoreDelegate;
import de.kaleidox.crystalshard.core.net.request.HttpMethod;
import de.kaleidox.crystalshard.core.net.request.endpoint.DiscordEndpoint;
import de.kaleidox.crystalshard.internal.InternalDelegate;
import de.kaleidox.crystalshard.main.items.user.AccountType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * This class is used to create a connection with Discord.
 */
public class DiscordLoginTool {
    private String token = null;
    private AccountType type = AccountType.BOT;
    private int shardCount = 1;
    private int shard = 0;

    /**
     * Creates a new instance.
     */
    public DiscordLoginTool() {
    }

    /**
     * Creates a new instance with a preset token.
     *
     * @param token The token to pre-set.
     */
    public DiscordLoginTool(String token) {
        this.token = token;
    }

    /**
     * Creates a new instance.
     *
     * @return The tool.
     */
    public static DiscordLoginTool get() {
        return new DiscordLoginTool();
    }

    /**
     * Sets the token for this tool.
     *
     * @param token The token to set.
     * @return The tool.
     */
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

    /**
     * Sets the shard count to discord's recommended shard count.
     *
     * @return The new instance of the DiscordLoginTool.
     * @deprecated Does not work like this yet. // todo Responds with 400 Bad Request
     */
    @Deprecated
    public DiscordLoginTool setRecommendedShardCount() {
        Objects.requireNonNull(token, "Token must be set first!");
        Discord login = InternalDelegate.newInstance(Discord.class, token);
        return CoreDelegate.webRequest(DiscordLoginTool.class, login)
                .setMethod(HttpMethod.GET)
                .setUri(DiscordEndpoint.GATEWAY_BOT.createUri())
                .executeAs(node -> setShardCount(
                        node.path("shards")
                                .asInt(1)))
                .join();
    }

    public Discord login() {
        return InternalDelegate.newInstance(Discord.class, token, type, shard, shardCount);
    }

    public CompletableFuture<Discord> loginWaitForServers() {
        return CompletableFuture.supplyAsync(() -> {
            Discord discordInternal = InternalDelegate.newInstance(Discord.class, token, type, shard, shardCount);
            while (!discordInternal.initFinished()) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {
                }
            }
            return discordInternal;
        });
    }

    public MultiShard loginMultiShard() {
        List<Discord> loggedIn = new ArrayList<>();
        for (int i = 0; i < shardCount; i++) {
            loggedIn.add(InternalDelegate.newInstance(Discord.class, token, type, i, shardCount));
        }
        return new MultiShard(loggedIn);
    }
}
