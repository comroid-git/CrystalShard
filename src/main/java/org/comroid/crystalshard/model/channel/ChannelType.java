package org.comroid.crystalshard.model.channel;

import org.comroid.api.IntEnum;
import org.comroid.api.Named;
import org.comroid.api.Rewrapper;
import org.comroid.common.info.Described;
import org.jetbrains.annotations.NotNull;

public enum ChannelType implements IntEnum, Named, Described {
    GUILD_TEXT(0, "a text channel within a server"),
    DM(1, "a direct message between users"),
    GUILD_VOICE(2, "a voice channel within a server"),
    GROUP_DM(3, "a direct message between multiple users"),
    GUILD_CATEGORY(4, "an organizational category that contains up to 50 channels"),
    GUILD_NEWS(5, "a channel that users can follow and crosspost into their own server"),
    GUILD_STORE(6, "a channel in which game developers can sell their game on Discord");

    private final int value;
    private final String description;

    @Override
    public @NotNull Integer getValue() {
        return value;
    }

    @Override
    public String getDescription() {
        return description;
    }

    ChannelType(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public static Rewrapper<ChannelType> valueOf(int value) {
        return IntEnum.valueOf(value, ChannelType.class);
    }
}
