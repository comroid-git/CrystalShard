package org.comroid.crystalshard.api.model.channel;

import org.jetbrains.annotations.Nullable;

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

    public static @Nullable ChannelType valueOf(int value) {
        for (ChannelType type : values()) 
            if (type.value == value)
                return type;
        
        return null;
    }
}
