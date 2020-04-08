package org.comroid.crystalshard.api.entity;

import org.jetbrains.annotations.ApiStatus.Internal;

import static org.comroid.crystalshard.util.Util.isFlagSet;

public @Internal enum EntityType {
    UNKNOWN(0x0000, 0x0000),

    GUILD(0x00000001, 0x00000001),

    AUDIT_LOG_ENTRY(0x00000002, 0x00000002),

    AUDIT_LOG_ENTRY_TARGET(0x00000004, 0x00000004),

    CUSTOM_EMOJI(0x00000008, 0x00000008),

    USER(0x00000010, 0x00000010),

    WEBHOOK(0x00000020, 0x00000020),

    ROLE(0x00000040, 0x00000040),

    MESSAGE(0x00000080, 0x00000080),

    CHANNEL(0x00000100, 0x00000100),

    TEXT_CHANNEL(0x00000200, CHANNEL.mask | 0x00000200),

    VOICE_CHANNEL(0x00000400, CHANNEL.mask | 0x00000400),

    GUILD_CHANNEL(0x00000800, CHANNEL.mask | 0x00000800),

    GUILD_TEXT_CHANNEL(0x00001000, TEXT_CHANNEL.mask | GUILD_CHANNEL.mask | 0x00001000),

    GUILD_VOICE_CHANNEL(0x00002000, VOICE_CHANNEL.mask | GUILD_CHANNEL.mask | 0x00002000),

    PRIVATE_TEXT_CHANNEL(0x00004000, TEXT_CHANNEL.mask | VOICE_CHANNEL.mask | 0x00004000),

    GROUP_TEXT_CHANNEL(0x00008000, TEXT_CHANNEL.mask | VOICE_CHANNEL.mask | 0x00008000),

    GUILD_CHANNEL_CATEGORY(0x00010000, GUILD_CHANNEL.mask | 0x00010000),

    GUILD_NEWS_CHANNEL(0x00040000, GUILD_CHANNEL.mask | 0x00040000),

    GUILD_STORE_CHANNEL(0x00080000, GUILD_CHANNEL.mask | 0x00080000),

    MESSAGE_ATTACHMENT(0x00020000, 0x00020000),

    PRIVATE_CHANNEL(0x000F0000, CHANNEL.mask | 0x000F0000),

    GUILD_MEMBER(0x00100000, USER.mask | 0x00100000);

    public @Internal final int mask;
    private final int flag;

    EntityType(int flag, int mask) {
        this.flag = flag;
        this.mask = mask;
    }

    public boolean isType(EntityType other) {
        return isFlagSet(flag, other.mask);
    }
}
