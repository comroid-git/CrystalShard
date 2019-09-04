package de.kaleidox.crystalshard.api.entity;

public enum EntityType {
    GUILD(0x00001, 0x00001),

    AUDIT_LOG_ENTRY(0x00002, 0x00002),

    AUDIT_LOG_ENTRY_TARGET(0x00004, 0x00004),

    CUSTOM_EMOJI(0x00008, 0x00008),

    USER(0x00010, 0x00010),

    WEBHOOK(0x00020, 0x00020),

    ROLE(0x00040, 0x00040),

    MESSAGE(0x00080, 0x00080),

    CHANNEL(0x00100, 0x00100),

    TEXT_CHANNEL(0x00200, CHANNEL.effectiveMask | 0x00200),

    VOICE_CHANNEL(0x00400, CHANNEL.effectiveMask | 0x00400),

    GUILD_CHANNEL(0x00800, CHANNEL.effectiveMask | 0x00800),

    GUILD_TEXT_CHANNEL(0x01000, TEXT_CHANNEL.effectiveMask | GUILD_CHANNEL.effectiveMask | 0x01000),

    GUILD_VOICE_CHANNEL(0x02000, VOICE_CHANNEL.effectiveMask | GUILD_CHANNEL.effectiveMask | 0x02000),

    PRIVATE_TEXT_CHANNEL(0x04000, TEXT_CHANNEL.effectiveMask | VOICE_CHANNEL.effectiveMask | 0x04000),

    GROUP_TEXT_CHANNEL(0x08000, TEXT_CHANNEL.effectiveMask | VOICE_CHANNEL.effectiveMask | 0x08000),

    GUILD_CHANNEL_CATEGORY(0x10000, GUILD_CHANNEL.effectiveMask | 0x10000),

    GUILD_NEWS_CHANNEL(0x40000, GUILD_CHANNEL.effectiveMask | 0x40000),

    GUILD_STORE_CHANNEL(0x80000, GUILD_CHANNEL.effectiveMask | 0x80000),

    MESSAGE_ATTACHMENT(0x20000, 0x20000),

    PRIVATE_CHANNEL(0xF0000, CHANNEL.effectiveMask | 0xF0000);

    private final int ownMask;
    private final int effectiveMask;

    EntityType(int ownMask, int effectiveMask) {
        this.ownMask = ownMask;
        this.effectiveMask = effectiveMask;
    }

    public boolean isType(EntityType other) {
        return (effectiveMask & other.ownMask) != 0;
    }
}
