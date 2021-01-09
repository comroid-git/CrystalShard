package org.comroid.crystalshard;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.comroid.api.ContextualProvider;
import org.comroid.common.Disposable;
import org.comroid.common.exception.AssertionException;
import org.comroid.crystalshard.entity.user.User;
import org.comroid.crystalshard.gateway.event.GatewayEvent;
import org.comroid.crystalshard.rest.Endpoint;
import org.comroid.crystalshard.rest.response.AbstractRestResponse;
import org.comroid.crystalshard.rest.response.GatewayBotResponse;
import org.comroid.mutatio.pipe.Pipe;
import org.comroid.mutatio.ref.Reference;
import org.comroid.mutatio.span.Span;
import org.comroid.restless.REST;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

public abstract class AbstractDiscordBot implements Bot {
    private static final Logger logger = LogManager.getLogger();
    private final DiscordAPI context;
    private final Span<DiscordBotShard> shards;
    private final GatewayBotResponse gbr;
    public final Reference<String> token;

    @Override
    public final SnowflakeCache getSnowflakeCache() {
        return context.getSnowflakeCache();
    }

    @Override
    public final Pipe<? extends GatewayEvent> getEventPipeline() {
        // todo ?? ?? ??
        return null;
    }

    @Override
    public final boolean isReady() {
        return shards.stream().allMatch(Bot::isReady);
    }

    @Override
    public final User getYourself() {
        return shards.stream()
                .map(DiscordBotShard::getYourself)
                .findAny()
                .orElseThrow(AssertionError::new);
    }

    @Override
    public final int getCurrentShardID() throws UnsupportedOperationException {
        if (getShardCount() == 1)
            return 0;
        return -1;
        //throw new UnsupportedOperationException("There is no specific Shard");
    }

    @Override
    public final int getShardCount() {
        return shards.size();
    }

    protected AbstractDiscordBot(DiscordAPI context, String token) {
        this.context = context;
        this.token = Reference.constant(token);
        this.gbr = newRequest(REST.Method.GET, Endpoint.GATEWAY_BOT).join();
        this.shards = IntStream.range(0, gbr.shards.assertion("shard count"))
                .mapToObj(shardIndex -> {
                    DiscordBotShard shard = new DiscordBotShard(context, token, gbr.uri.assertion("ws uri"), shardIndex);
                    AssertionException.expect(shardIndex, shard.getCurrentShardID(), "shardID");
                    return shard;
                })
                .collect(Span.collector());
    }

    @Override
    public final <R extends AbstractRestResponse> CompletableFuture<R> newRequest(REST.Method method, Endpoint<R> endpoint) {
        return DiscordAPI.newRequest(context, token.assertion(), method, endpoint);
    }

    @Override
    public String getToken() {
        return token.assertion();
    }

    @Override
    public final void close() throws IOException, Disposable.MultipleExceptions {
        final List<IOException> exceptions = new ArrayList<>();

        shards.removeIf(discordBotShard -> {
            try {
                discordBotShard.close();
            } catch (IOException e) {
                exceptions.add(e);
                return false;
            }
            return true;
        });

        //noinspection ConstantConditions -> false positive
        if (exceptions.size() == 0)
            return;
        if (exceptions.size() == 1)
            throw exceptions.get(0);
        throw new Disposable.MultipleExceptions(String.format("Failed to close %d shards", shards.size()), exceptions);
    }

    @Override
    public final ContextualProvider getUnderlyingContextualProvider() {
        return context.plus(this);
    }
}
