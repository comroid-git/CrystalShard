package de.kaleidox.crystalshard.main;

import de.kaleidox.crystalshard.core.net.request.Endpoint;
import de.kaleidox.crystalshard.core.net.request.Method;
import de.kaleidox.crystalshard.core.net.request.WebRequest;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.main.handling.listener.ListenerAttachable;
import de.kaleidox.crystalshard.main.handling.listener.ListenerManager;
import de.kaleidox.crystalshard.main.handling.listener.PreAttachableListener;
import de.kaleidox.crystalshard.main.items.user.AccountType;
import de.kaleidox.util.objects.Evaluation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class is used to create a connection with Discord.
 */
public class DiscordLoginTool implements ListenerAttachable<PreAttachableListener> {
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
        Discord login = new DiscordInternal(token);
        return new WebRequest<DiscordLoginTool>(login)
                .method(Method.GET)
                .endpoint(Endpoint.Location.GATEWAY_BOT.toEndpoint())
                .execute(node -> setShardCount(node.path("shards").asInt(1)))
                .join();
    }

    public Discord login() {
        return new DiscordInternal(token, type, shard, shardCount);
    }

    public MultiShard loginMultiShard() {
        List<Discord> loggedIn = new ArrayList<>();
        for (int i = 0; i < shardCount; i++) {
            loggedIn.add(new DiscordInternal(token, type, i, shardCount));
        }
        return new MultiShard(loggedIn);
    }

    @Override
    public Evaluation<Boolean> detachListener(PreAttachableListener listener) {
        return Evaluation.of(false);
    }

    @Override
    public <C extends PreAttachableListener> ListenerManager<C> attachListener(C listener) {
        return null; // todo
    }
}
