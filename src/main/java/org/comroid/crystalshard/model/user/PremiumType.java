package org.comroid.crystalshard.model.user;

import org.comroid.api.IntEnum;
import org.comroid.api.Rewrapper;
import org.jetbrains.annotations.NotNull;

public enum PremiumType implements IntEnum {
    NONE(0),
    NITRO_CLASSIC(1),
    NITRO(2);

    private final int value;

    @Override
    public @NotNull Integer getValue() {
        return value;
    }

    PremiumType(int value) {
        this.value = value;
    }

    public static Rewrapper<PremiumType> valueOf(int value) {
        return IntEnum.valueOf(value, PremiumType.class);
    }
}
