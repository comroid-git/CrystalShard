package org.comroid.crystalshard.event;

import org.comroid.api.Polyfill;
import org.comroid.crystalshard.DiscordAPI;
import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.core.gateway.event.GatewayPayload;
import org.comroid.listnr.EventType;
import org.comroid.mutatio.proc.Processor;
import org.comroid.trie.TrieMap;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.BiFunction;

public final class DiscordBotEvent<DBP extends DiscordBotPayload> implements EventType<DiscordBot, GatewayPayload, DiscordBotPayload> {
    public static final Map<String, DiscordBotEvent<? extends DiscordBotPayload>> cache = TrieMap.ofString();

    private final String name;
    private final BiFunction<GatewayPayload, DiscordBot, DBP> payloadConstructor;
    private final Processor<EventType<?, ?, GatewayPayload>> commonCause;

    @Override
    public Processor<EventType<?, ?, GatewayPayload>> getCommonCause() {
        return commonCause;
    }

    @Override
    public String getName() {
        return name;
    }

    public DiscordBotEvent(
            String name,
            EventType<?, ?, GatewayPayload> commonCause,
            BiFunction<GatewayPayload, DiscordBot, DBP> payloadConstructor
    ) {
        this.name = name;
        this.payloadConstructor = payloadConstructor;
        this.commonCause = Processor.ofConstant(commonCause);

        cache.put(name, this);
    }

    public static @Nullable
    DiscordBotEvent<? extends GatewayPayload> valueOf(String name) {
        return Polyfill.uncheckedCast(cache.get(name));
    }

    @Override
    public boolean triggeredBy(GatewayPayload data) {
        return getCommonCause().ifPresentMapOrElseGet(data.getEventType()::equals, () -> false);
    }

    @Override
    public DBP createPayload(GatewayPayload data, DiscordBot bot) {
        return payloadConstructor.apply(data, bot);
    }
}
