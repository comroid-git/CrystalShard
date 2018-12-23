package de.kaleidox.crystalshard.api;

import de.kaleidox.crystalshard.core.CoreInjector;
import de.kaleidox.crystalshard.core.net.request.HttpMethod;
import de.kaleidox.crystalshard.core.net.request.WebRequest;
import de.kaleidox.crystalshard.core.net.request.endpoint.DiscordEndpoint;
import de.kaleidox.crystalshard.internal.InternalInjector;
import de.kaleidox.crystalshard.api.entity.user.AccountType;

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
        Discord login = InternalInjector.newInstance(Discord.class, token);
        //noinspection unchecked
        ((WebRequest<DiscordLoginTool>) CoreInjector.newInstance(WebRequest.class))
                .addHeader("Authentication", type.getPrefix() + token)
                .setMethod(HttpMethod.GET)
                .setUri(DiscordEndpoint.GATEWAY_BOT.createUri())
                .executeAs(node -> setShardCount(node.path("shards").asInt(1)))
                .join();
        return this;
    }

    public DiscordLoginTool setShardCount(int count) {
        this.shardCount = count;
        return this;
    }

    public Discord login() {
        return InternalInjector.newInstance(Discord.class, token, type, shard, shardCount);
    }

    public CompletableFuture<Discord> loginWaitForServers() {
        return CompletableFuture.supplyAsync(() -> {
            Discord discordInternal = InternalInjector.newInstance(Discord.class, token, type, shard, shardCount);
            while (!discordInternal.initFinished()) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {
                }
            }
            return discordInternal;
        });
    }

    /**
     * Creates a new instance.
     *
     * @return The tool.
     */
    public static DiscordLoginTool get() {
        return new DiscordLoginTool();
    }
}
