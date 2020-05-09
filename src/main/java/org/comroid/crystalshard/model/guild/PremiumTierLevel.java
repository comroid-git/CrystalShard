package org.comroid.crystalshard.model.guild;

import org.comroid.common.ref.IntEnum;

public enum PremiumTierLevel implements IntEnum {
    NONE(0),
    TIER_1(1),
    TIER_2(2),
    TIER_3(3);

    private final int value;

    @Override
    public int getValue() {
        return value;
    }

    PremiumTierLevel(int value) {
        this.value = value;
    }

    public static PremiumTierLevel valueOf(int value) {
        for (PremiumTierLevel level : values()) {
            if (level.value == value)
                return level;
        }

        throw new IllegalArgumentException("Unknown PremiumTierLevel: " + value);
    }
}
