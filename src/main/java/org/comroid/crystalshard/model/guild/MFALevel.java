package org.comroid.crystalshard.model.guild;

import org.comroid.common.ref.IntEnum;

public enum MFALevel implements IntEnum {
    NONE(0),
    ELEVATED(1);

    private final int value;

    @Override
    public int getValue() {
        return value;
    }

    MFALevel(int value) {
        this.value = value;
    }

    public static MFALevel valueOf(int value) {
        for (MFALevel level : values()) {
            if (level.value == value)
                return level;
        }

        throw new IllegalArgumentException("Unknown MFALevel: " + value);
    }
}
