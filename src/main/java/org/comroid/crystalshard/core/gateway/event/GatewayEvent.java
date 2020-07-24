package org.comroid.crystalshard.core.gateway.event;

import org.comroid.api.Polyfill;
import org.comroid.crystalshard.DiscordAPI;
import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.core.gateway.payload.generic.GatewayHelloPayload;
import org.comroid.crystalshard.core.gateway.payload.generic.GatewayReadyPayload;
import org.comroid.crystalshard.core.gateway.payload.generic.GatewayResumedPayload;
import org.comroid.listnr.EventType;
import org.comroid.mutatio.proc.Processor;
import org.comroid.mutatio.span.Span;
import org.comroid.restless.socket.event.WebSocketEvent;
import org.comroid.restless.socket.event.WebSocketPayload;
import org.comroid.trie.TrieMap;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Function;

public final class GatewayEvent<GP extends GatewayPayload> implements EventType<DiscordBot, WebSocketPayload.Data, GP> {
    public static final GatewayEvent<GatewayHelloPayload> HELLO
            = new GatewayEvent<>("HELLO", null, GatewayHelloPayload::new);
    public static final GatewayEvent<GatewayReadyPayload> READY
            = new GatewayEvent<>("READY", null, GatewayReadyPayload::new);
    public static final GatewayEvent<GatewayResumedPayload> RESUMED
            = new GatewayEvent<>("RESUMED", null, GatewayResumedPayload::new);
    public static final GatewayEvent RECONNECT
            = new GatewayEvent<>("RECONNECT");
    public static final GatewayEvent INVALID_SESSION
            = new GatewayEvent<>("INVALID_SESSION");
    public static final GatewayEvent CHANNEL_CREATE
            = new GatewayEvent<>("CHANNEL_CREATE");
    public static final GatewayEvent CHANNEL_UPDATE
            = new GatewayEvent<>("CHANNEL_UPDATE");
    public static final GatewayEvent CHANNEL_DELETE
            = new GatewayEvent<>("CHANNEL_DELETE");
    public static final GatewayEvent CHANNEL_PINS_UPDATE
            = new GatewayEvent<>("CHANNEL_PINS_UPDATE");
    public static final GatewayEvent GUILD_CREATE
            = new GatewayEvent<>("GUILD_CREATE");
    public static final GatewayEvent GUILD_UPDATE
            = new GatewayEvent<>("GUILD_UPDATE");
    public static final GatewayEvent GUILD_DELETE
            = new GatewayEvent<>("GUILD_DELETE");
    public static final GatewayEvent GUILD_BAN_ADD
            = new GatewayEvent<>("GUILD_BAN_ADD");
    public static final GatewayEvent GUILD_BAN_REMOVE
            = new GatewayEvent<>("GUILD_BAN_REMOVE");
    public static final GatewayEvent GUILD_EMOJIS_UPDATE
            = new GatewayEvent<>("GUILD_EMOJIS_UPDATE");
    public static final GatewayEvent GUILD_INTEGRATIONS_UPDATE
            = new GatewayEvent<>("GUILD_INTEGRATIONS_UPDATE");
    public static final GatewayEvent GUILD_MEMBER_ADD
            = new GatewayEvent<>("GUILD_MEMBER_ADD");
    public static final GatewayEvent GUILD_MEMBER_REMOVE
            = new GatewayEvent<>("GUILD_MEMBER_REMOVE");
    public static final GatewayEvent GUILD_MEMBER_UPDATE
            = new GatewayEvent<>("GUILD_MEMBER_UPDATE");
    public static final GatewayEvent GUILD_MEMBERS_CHUNK
            = new GatewayEvent<>("GUILD_MEMBERS_CHUNK");
    public static final GatewayEvent GUILD_ROLE_CREATE
            = new GatewayEvent<>("GUILD_ROLE_CREATE");
    public static final GatewayEvent GUILD_ROLE_UPDATE
            = new GatewayEvent<>("GUILD_ROLE_UPDATE");
    public static final GatewayEvent GUILD_ROLE_DELETE
            = new GatewayEvent<>("GUILD_ROLE_DELETE");
    public static final GatewayEvent INVITE_CREATE
            = new GatewayEvent<>("INVITE_CREATE");
    public static final GatewayEvent INVITE_DELETE
            = new GatewayEvent<>("INVITE_DELETE");
    public static final GatewayEvent MESSAGE_CREATE
            = new GatewayEvent<>("MESSAGE_CREATE");
    public static final GatewayEvent MESSAGE_UPDATE
            = new GatewayEvent<>("MESSAGE_UPDATE");
    public static final GatewayEvent MESSAGE_DELETE
            = new GatewayEvent<>("MESSAGE_DELETE");
    public static final GatewayEvent MESSAGE_DELETE_BULK
            = new GatewayEvent<>("MESSAGE_DELETE_BULK");
    public static final GatewayEvent MESSAGE_REACTION_ADD
            = new GatewayEvent<>("MESSAGE_REACTION_ADD");
    public static final GatewayEvent MESSAGE_REACTION_REMOVE
            = new GatewayEvent<>("MESSAGE_REACTION_REMOVE");
    public static final GatewayEvent MESSAGE_REACTION_REMOVE_ALL
            = new GatewayEvent<>("MESSAGE_REACTION_REMOVE_ALL");
    public static final GatewayEvent MESSAGE_REACTION_REMOVE_EMOJI
            = new GatewayEvent<>("MESSAGE_REACTION_REMOVE_EMOJI");
    public static final GatewayEvent PRESENCE_UPDATE
            = new GatewayEvent<>("PRESENCE_UPDATE");
    public static final GatewayEvent TYPING_START
            = new GatewayEvent<>("TYPING_START");
    public static final GatewayEvent USER_UPDATE
            = new GatewayEvent<>("USER_UPDATE");
    public static final GatewayEvent VOICE_STATE_UPDATE
            = new GatewayEvent<>("VOICE_STATE_UPDATE");
    public static final GatewayEvent VOICE_SERVER_UPDATE
            = new GatewayEvent<>("VOICE_SERVER_UPDATE");
    public static final GatewayEvent WEBHOOKS_UPDATE
            = new GatewayEvent<>("WEBHOOKS_UPDATE");

    public static final Map<String, GatewayEvent> cache = TrieMap.ofString();

    private final String name;
    private final @Nullable DiscordAPI.Intent intent;
    private final Function<GatewayPayloadWrapper, GP> payloadConstructor;

    @Override
    public Processor<EventType<?, ?, WebSocketPayload.Data>> getCommonCause() {
        return Processor.ofConstant(WebSocketEvent.DATA);
    }

    @Override
    public String getName() {
        return name;
    }

    public Processor<DiscordAPI.Intent> getIntent() {
        return Processor.ofConstant(intent);
    }

    private GatewayEvent(String name, DiscordAPI.Intent intent, Function<GatewayPayloadWrapper, GP> payloadConstructor) {
        this.name = name;
        this.intent = intent;
        this.payloadConstructor = payloadConstructor;

        cache.put(name, this);
    }

    public static @Nullable GatewayEvent<? extends GatewayPayload> valueOf(String name) {
        return Polyfill.uncheckedCast(cache.get(name));
    }

    @Override
    public boolean triggeredBy(WebSocketPayload.Data data) {
        return GatewayPayloadWrapper.EventType.getFrom(data.getBody().asObjectNode()).equals(this);
    }

    @Override
    public GP createPayload(WebSocketPayload.Data data, DiscordBot bot) {
        final GatewayPayloadWrapper gpw = new GatewayPayloadWrapper(bot, data);
        return payloadConstructor.apply(gpw);
    }

    @Override
    public boolean equals(Object other) {
        //noinspection rawtypes
        return (other instanceof GatewayEvent) && ((GatewayEvent) other).name.equals(name);
    }
}
