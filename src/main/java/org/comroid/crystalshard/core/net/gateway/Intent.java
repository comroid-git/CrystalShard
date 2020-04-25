package org.comroid.crystalshard.core.net.gateway;

import org.comroid.common.util.Bitmask;

public enum Intent {
    GUILDS(1),
    GUILD_MEMBERS(1 << 1),
    GUILD_BANS(1 << 2),
    GUILD_EMOJIS(1 << 3),
    GUILD_INTEGRATIONS(1 << 4),
    GUILD_WEBHOOKS(1 << 5),
    GUILD_INVITES(1 << 7),
    GUILD_VOICE_STATES(1 << 7),
    GUILD_PRESENCES(1 << 8),
    GUILD_MESSAGES(1 << 9),
    GUILD_MESSAGE_REACTIONS(1 << 10),
    GUILD_MESSAGE_TYPING(1 << 11),
    DIRECT_MESSAGES(1 << 12),
    DIRECT_MESSAGE_REACTIONS(1 << 13),
    DIRECT_MESSAGE_TYPING(1 << 14);

    private final int mask;

    Intent(int mask) {
        this.mask = mask;
    }

    public static int combine(Intent... intents) {
        return Bitmask.combine(Intent::getMask, intents);
    }

    int getMask() {
        return mask;
    }
}
