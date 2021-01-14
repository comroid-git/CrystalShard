package org.comroid.crystalshard.gateway;

import org.comroid.api.BitmaskEnum;
import org.comroid.crystalshard.gateway.event.DispatchEventType;

import java.util.Set;

import static org.comroid.crystalshard.gateway.event.DispatchEventType.*;

@SuppressWarnings("PointlessBitwiseExpression")
public enum GatewayIntent implements BitmaskEnum<GatewayIntent> {
    GUILDS(1 << 0, GUILD_CREATE),
    GUILD_MEMBERS(1 << 1),
    GUILD_BANS(1 << 2),
    GUILD_EMOJIS(1 << 3),
    GUILD_INTEGRATIONS(1 << 4),
    GUILD_WEBHOOKS(1 << 5),
    GUILD_INVITES(1 << 6),
    GUILD_VOICE_STATES(1 << 7),
    GUILD_PRESENCES(1 << 8),
    GUILD_MESSAGES(1 << 9),
    GUILD_MESSAGE_REACTIONS(1 << 10),
    GUILD_MESSAGE_TYPING(1 << 11),

    DIRECT_MESSAGES(1 << 12),
    DIRECT_MESSAGE_REACTIONS(1 << 13),
    DIRECT_MESSAGE_TYPING(1 << 14);

    private final int value;
    private final DispatchEventType[] dispatchEventTypes;

    @Override
    public int getValue() {
        return value;
    }

    GatewayIntent(int value, DispatchEventType... dispatchEventTypes) {
        this.value = value;
        this.dispatchEventTypes = dispatchEventTypes;
    }

    public static Set<GatewayIntent> valueOf(int mask) {
        return BitmaskEnum.valueOf(mask, GatewayIntent.class);
    }
}
