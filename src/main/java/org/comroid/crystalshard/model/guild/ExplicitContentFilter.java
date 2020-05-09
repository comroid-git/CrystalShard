package org.comroid.crystalshard.model.guild;

import org.comroid.common.ref.IntEnum;

public enum ExplicitContentFilter implements IntEnum {
    DISABLED(0),
    MEMBERS_WITHOUT_ROLES(1),
    ALL_MEMBERS(2);

    private final int value;

    @Override
    public int getValue() {
        return value;
    }

    ExplicitContentFilter(int value) {
        this.value = value;
    }

    public static ExplicitContentFilter valueOf(int value) {
        for (ExplicitContentFilter level : values()) {
            if (level.value == value)
                return level;
        }

        throw new IllegalArgumentException("Unknown ExplicitContentFilter level: " + value);
    }
}
