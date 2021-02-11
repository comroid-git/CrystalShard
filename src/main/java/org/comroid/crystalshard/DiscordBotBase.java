package org.comroid.crystalshard;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.comroid.api.ContextualProvider;
import org.comroid.common.Disposable;
import org.comroid.common.exception.AssertionException;
import org.comroid.crystalshard.entity.user.User;
import org.comroid.crystalshard.gateway.GatewayIntent;
import org.comroid.crystalshard.gateway.event.GatewayEvent;
import org.comroid.crystalshard.gateway.presence.BotBasedPresence;
import org.comroid.crystalshard.rest.Endpoint;
import org.comroid.crystalshard.rest.response.GatewayBotResponse;
import org.comroid.crystalshard.ui.InteractionCore;
import org.comroid.mutatio.pipe.Pipe;
import org.comroid.mutatio.pump.Pump;
import org.comroid.mutatio.ref.Reference;
import org.comroid.mutatio.span.Span;
import org.comroid.restless.REST;
import org.comroid.restless.endpoint.CompleteEndpoint;
import org.comroid.uniform.node.UniNode;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.container.DataContainer;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.IntStream;

public class DiscordBotBase implements Bot {
    private static final Logger logger = LogManager.getLogger();
    private final DiscordAPI context;
    private final Set<GatewayIntent> intents;
    private final Span<DiscordBotShard> shards;
    private final Pipe<? extends GatewayEvent> eventPipeline;
    private final BotBasedPresence ownPresence;
    private final InteractionCore interactionCore;
    protected final Reference<String> token;

    public Span<DiscordBotShard> getShards() {
        return shards;
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public final Pipe<? extends GatewayEvent> getEventPipeline() {
        return eventPipeline;
    }

    public InteractionCore getInteractionCore() {
        return interactionCore;
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

    @Override
    public BotBasedPresence getOwnPresence() {
        return ownPresence;
    }

    public DiscordBotBase(DiscordAPI context, String token, GatewayIntent... intents) {
        if (intents.length == 0)
            intents = GatewayIntent.ALL_UNPRIVILEGED;
        context.members.add(this);
        this.context = context;
        this.token = Reference.constant(token);
        this.intents = new HashSet<>(Arrays.asList(intents));

        // start connection
        final GatewayBotResponse gbr = newRequest(REST.Method.GET, Endpoint.GATEWAY_BOT, GatewayBotResponse.TYPE).join();
        int shardCount = gbr.shards.assertion("shard count");

        /*
        if (gbr.sessionStartLimit.test(GatewayBotResponse.SessionStartLimit::isBlocked))
            throw new IllegalStateException("Cannot connect; No remaining Session Starts");
         */

        this.shards = IntStream.range(0, shardCount)
                .mapToObj(shardIndex -> {
                    DiscordBotShard shard = new DiscordBotShard(context, token,
                            gbr.uri.assertion("ws uri"), shardIndex, shardCount, this.intents.toArray(new GatewayIntent[0]));
                    AssertionException.expect(shardIndex, shard.getCurrentShardID(), "shardID");
                    return shard;
                })
                .collect(Span.collector());
        this.eventPipeline = Pump.combine(shards.stream().map(DiscordBotShard::getEventPipeline));
        this.ownPresence = new BotBasedPresence(this, shards);
        this.interactionCore = new InteractionCore(this);
    }

    @Override
    public String getToken() {
        return token.assertion();
    }

    @Override
    public final void close() throws IOException, Disposable.MultipleExceptions {
        logger.info("Shutting down DiscordBotBase <{}>", getClass().getSimpleName());
        final List<IOException> exceptions = new ArrayList<>();

        shards.forEach(discordBotShard -> {
            try {
                discordBotShard.close();
            } catch (IOException e) {
                exceptions.add(e);
            }
        });

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
