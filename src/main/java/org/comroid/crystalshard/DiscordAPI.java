package org.comroid.crystalshard;

import org.comroid.api.BitmaskEnum;

public final class DiscordAPI {
    public static final int API_VERSION = 6;
    public static final long EPOCH = 1420070400000L;
    public static final String URL_BASE = "https://discordapp.com/api/v" + API_VERSION;

    public enum Intent implements BitmaskEnum<Intent> {
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

        @Override
        public int getValue() {
            return mask;
        }

        Intent(int mask) {
            this.mask = mask;
        }
    }
}
