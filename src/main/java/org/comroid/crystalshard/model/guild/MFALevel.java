package org.comroid.crystalshard.model.guild;

import org.comroid.api.IntEnum;
import org.comroid.api.Named;
import org.comroid.api.Rewrapper;
import org.jetbrains.annotations.NotNull;

public enum MFALevel implements IntEnum, Named {
    NONE(0),
    ELEVATED(1);

    private final int value;

    @Override
    public @NotNull Integer getValue() {
        return value;
    }

    MFALevel(int value) {
        this.value = value;
    }

    public static Rewrapper<MFALevel> valueOf(int value) {
        return IntEnum.valueOf(value, MFALevel.class);
    }
}
