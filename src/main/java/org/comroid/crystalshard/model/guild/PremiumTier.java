package org.comroid.crystalshard.model.guild;

import org.comroid.api.IntEnum;
import org.comroid.api.Named;
import org.comroid.api.Rewrapper;

public enum PremiumTier implements IntEnum, Named {
    NONE(0),
    TIER_1(1),
    TIER_2(2),
    TIER_3(3);

    private final int value;

    @Override
    public int getValue() {
        return value;
    }

    PremiumTier(int value) {
        this.value = value;
    }

    public static Rewrapper<PremiumTier> valueOf(int value) {
        return IntEnum.valueOf(value, PremiumTier.class);
    }
}
