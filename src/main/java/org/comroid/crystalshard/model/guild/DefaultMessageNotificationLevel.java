package org.comroid.crystalshard.model.guild;

import org.comroid.api.IntEnum;
import org.comroid.api.Named;
import org.comroid.api.Rewrapper;

public enum DefaultMessageNotificationLevel implements IntEnum, Named {
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

    public static Rewrapper<DefaultMessageNotificationLevel> valueOf(int value) {
        return IntEnum.valueOf(value, DefaultMessageNotificationLevel.class);
    }
}