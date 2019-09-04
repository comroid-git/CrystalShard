package de.kaleidox.crystalshard.api.model.channel;

public enum ChannelType {
    GUILD_TEXT(0),

    DM(1),

    GUILD_VOICE(2),

    GROUP_DM(3),

    GUILD_CATEGORY(4),

    GUILD_NEWS(5),

    GUILD_STORE(6);

    public final int value;

    ChannelType(int value) {
        this.value = value;
    }
}
