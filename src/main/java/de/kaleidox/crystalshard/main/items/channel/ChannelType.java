package de.kaleidox.crystalshard.main.items.channel;

import java.util.stream.Stream;

public enum ChannelType {
    UNKNOWN(-1),
    GUILD_TEXT(0),
    DM(1),
    GUILD_VOICE(2),
    GROUP_DM(3),
    GUILD_CATEGORY(4);

    private int id;

    ChannelType(int id) {
        this.id = id;
    }

    public static ChannelType getFromId(int id) {
        return Stream.of(values())
                .filter(type -> type.id == id)
                .findAny()
                .orElse(UNKNOWN);
    }
}
