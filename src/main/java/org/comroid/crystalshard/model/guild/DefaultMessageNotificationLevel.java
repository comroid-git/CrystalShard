package org.comroid.crystalshard.model.guild;

import org.comroid.common.ref.IntEnum;

public enum DefaultMessageNotificationLevel implements IntEnum {
    ALL_MESSAGES(0),
    ONLY_MENTIONS(1);

    private final int value;

    @Override
    public int getValue() {
        return value;
    }

    DefaultMessageNotificationLevel(int value) {
        this.value = value;
    }

    public static DefaultMessageNotificationLevel valueOf(int value) {
        for (DefaultMessageNotificationLevel level : values()) {
            if (level.value == value)
                return level;
        }

        throw new IllegalArgumentException("Unknown DefaultMessageNotificationLevel: " + value);
    }
}
