package org.comroid.crystalshard.core.event;

import org.comroid.api.Polyfill;
import org.comroid.common.ref.Named;
import org.comroid.crystalshard.core.gateway.payload.AbstractGatewayPayload;
import org.comroid.crystalshard.core.gateway.payload.generic.GatewayHelloPayload;
import org.comroid.crystalshard.core.gateway.payload.generic.GatewayReadyPayload;
import org.comroid.crystalshard.core.gateway.payload.generic.GatewayResumedPayload;
import org.comroid.crystalshard.event.DiscordBotPayload;
import org.comroid.listnr.EventType;
import org.comroid.trie.TrieMap;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Function;

public final class GatewayEventDefinition<GP extends AbstractGatewayPayload>
        implements EventType<GatewayPayloadWrapper, GP>, Named {
    public static final GatewayEventDefinition HELLO
            = new GatewayEventDefinition<>("HELLO", GatewayHelloPayload::new);
    public static final GatewayEventDefinition READY
            = new GatewayEventDefinition<>("READY", GatewayReadyPayload::new);
    public static final GatewayEventDefinition RESUMED
            = new GatewayEventDefinition<>("RESUMED", GatewayResumedPayload::new);
    public static final GatewayEventDefinition RECONNECT
            = new GatewayEventDefinition<>("RECONNECT");
    public static final GatewayEventDefinition INVALID_SESSION
            = new GatewayEventDefinition<>("INVALID_SESSION");

    public static final GatewayEventDefinition CHANNEL_CREATE
            = new GatewayEventDefinition<>("CHANNEL_CREATE");
    public static final GatewayEventDefinition CHANNEL_UPDATE
            = new GatewayEventDefinition<>("CHANNEL_UPDATE");
    public static final GatewayEventDefinition CHANNEL_DELETE
            = new GatewayEventDefinition<>("CHANNEL_DELETE");

    public static final GatewayEventDefinition CHANNEL_PINS_UPDATE
            = new GatewayEventDefinition<>("CHANNEL_PINS_UPDATE");

    public static final GatewayEventDefinition GUILD_CREATE
            = new GatewayEventDefinition<>("GUILD_CREATE");
    public static final GatewayEventDefinition GUILD_UPDATE
            = new GatewayEventDefinition<>("GUILD_UPDATE");
    public static final GatewayEventDefinition GUILD_DELETE
            = new GatewayEventDefinition<>("GUILD_DELETE");

    public static final GatewayEventDefinition GUILD_BAN_ADD
            = new GatewayEventDefinition<>("GUILD_BAN_ADD");
    public static final GatewayEventDefinition GUILD_BAN_REMOVE
            = new GatewayEventDefinition<>("GUILD_BAN_REMOVE");

    public static final GatewayEventDefinition GUILD_EMOJIS_UPDATE
            = new GatewayEventDefinition<>("GUILD_EMOJIS_UPDATE");

    public static final GatewayEventDefinition GUILD_INTEGRATIONS_UPDATE
            = new GatewayEventDefinition<>("GUILD_INTEGRATIONS_UPDATE");

    public static final GatewayEventDefinition GUILD_MEMBER_ADD
            = new GatewayEventDefinition<>("GUILD_MEMBER_ADD");
    public static final GatewayEventDefinition GUILD_MEMBER_REMOVE
            = new GatewayEventDefinition<>("GUILD_MEMBER_REMOVE");
    public static final GatewayEventDefinition GUILD_MEMBER_UPDATE
            = new GatewayEventDefinition<>("GUILD_MEMBER_UPDATE");
    public static final GatewayEventDefinition GUILD_MEMBERS_CHUNK
            = new GatewayEventDefinition<>("GUILD_MEMBERS_CHUNK");

    public static final GatewayEventDefinition GUILD_ROLE_CREATE
            = new GatewayEventDefinition<>("GUILD_ROLE_CREATE");
    public static final GatewayEventDefinition GUILD_ROLE_UPDATE
            = new GatewayEventDefinition<>("GUILD_ROLE_UPDATE");
    public static final GatewayEventDefinition GUILD_ROLE_DELETE
            = new GatewayEventDefinition<>("GUILD_ROLE_DELETE");

    public static final GatewayEventDefinition INVITE_CREATE
            = new GatewayEventDefinition<>("INVITE_CREATE");
    public static final GatewayEventDefinition INVITE_DELETE
            = new GatewayEventDefinition<>("INVITE_DELETE");

    public static final GatewayEventDefinition MESSAGE_CREATE
            = new GatewayEventDefinition<>("MESSAGE_CREATE");
    public static final GatewayEventDefinition MESSAGE_UPDATE
            = new GatewayEventDefinition<>("MESSAGE_UPDATE");
    public static final GatewayEventDefinition MESSAGE_DELETE
            = new GatewayEventDefinition<>("MESSAGE_DELETE");
    public static final GatewayEventDefinition MESSAGE_DELETE_BULK
            = new GatewayEventDefinition<>("MESSAGE_DELETE_BULK");

    public static final GatewayEventDefinition MESSAGE_REACTION_ADD
            = new GatewayEventDefinition<>("MESSAGE_REACTION_ADD");
    public static final GatewayEventDefinition MESSAGE_REACTION_REMOVE
            = new GatewayEventDefinition<>("MESSAGE_REACTION_REMOVE");
    public static final GatewayEventDefinition MESSAGE_REACTION_REMOVE_ALL
            = new GatewayEventDefinition<>("MESSAGE_REACTION_REMOVE_ALL");
    public static final GatewayEventDefinition MESSAGE_REACTION_REMOVE_EMOJI
            = new GatewayEventDefinition<>("MESSAGE_REACTION_REMOVE_EMOJI");

    public static final GatewayEventDefinition PRESENCE_UPDATE
            = new GatewayEventDefinition<>("PRESENCE_UPDATE");

    public static final GatewayEventDefinition TYPING_START
            = new GatewayEventDefinition<>("TYPING_START");

    public static final GatewayEventDefinition USER_UPDATE
            = new GatewayEventDefinition<>("USER_UPDATE");

    public static final GatewayEventDefinition VOICE_STATE_UPDATE
            = new GatewayEventDefinition<>("VOICE_STATE_UPDATE");
    public static final GatewayEventDefinition VOICE_SERVER_UPDATE
            = new GatewayEventDefinition<>("VOICE_SERVER_UPDATE");

    public static final GatewayEventDefinition WEBHOOKS_UPDATE
            = new GatewayEventDefinition<>("WEBHOOKS_UPDATE");

    public static final Map<String, GatewayEventDefinition> cache = TrieMap.ofString();

    private final String name;
    private final Function<GatewayPayloadWrapper, GP> payloadConstructor;

    @Override
    public String getName() {
        return name;
    }

    private GatewayEventDefinition(String name, Function<GatewayPayloadWrapper, GP> payloadConstructor) {
        this.name = name;
        this.payloadConstructor = payloadConstructor;

        cache.put(name, this);
    }

    public static @Nullable GatewayEventDefinition<? extends DiscordBotPayload> valueOf(String name) {
        return Polyfill.uncheckedCast(cache.get(name));
    }

    @Override
    public boolean test(GatewayPayloadWrapper payload) {
        return payload.process(GatewayPayloadWrapper.EventType)
                .into(this::equals);
    }

    @Override
    public GP apply(GatewayPayloadWrapper payload) {
        return payloadConstructor.apply(payload);
    }
}
