package org.comroid.crystalshard.model.guild;

import org.comroid.common.info.Described;
import org.comroid.common.ref.IntEnum;

public enum GuildVerificationLevel implements IntEnum, Described {
    NONE(0, "unrestricted"),
    LOW(1, "must have verified email on account"),
    MEDIUM(2, "must be registered on Discord for longer than 5 minutes"),
    HIGH(3, "(╯°□°）╯︵ ┻━┻ - must be a member of the server for longer than 10 minutes"),
    VERY_HIGH(4, "┻━┻ ミヽ(ಠ 益 ಠ)ﾉ彡 ┻━┻ - must have a verified phone number");

    private final int value;
    private final String description;

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public String getDescription() {
        return description;
    }

    GuildVerificationLevel(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public static GuildVerificationLevel valueOf(int value) {
        for (GuildVerificationLevel level : values()) {
            if (level.value == value)
                return level;
        }

        throw new IllegalArgumentException("Unknown GuildVerificationLevel: " + value);
    }
}
