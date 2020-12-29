package org.comroid.crystalshard;

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
        throw new UnsupportedOperationException("There is no specific Shard");
    }

    @Override
    public final int getShardCount() {
        return shards.size();
    }

    protected AbstractDiscordBot(DiscordAPI context, String token) {
        this.context = context;
        this.token = Reference.create(token);
        this.gbr = newRequest(REST.Method.GET, Endpoint.GATEWAY_BOT).join();
        this.shards = IntStream.range(0, gbr.shards.assertion())
                .mapToObj(shardIndex -> {
                    DiscordBotShard shard = new DiscordBotShard(context, token, shardIndex);
                    AssertionException.expect(shardIndex, shard.getCurrentShardID(), "shardID");
                    return shard;
                })
                .collect(Span.collector());
    }

    @Override
    public final <R extends AbstractRestResponse> CompletableFuture<R> newRequest(REST.Method method, Endpoint<R> endpoint) {
        return context.getREST()
                .request(endpoint)
                .addHeaders(token.into(DiscordBotShard::createHeaders))
                .method(method)
                .execute$deserializeSingle();
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
