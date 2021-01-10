package org.comroid.crystalshard.model.guild;

import org.comroid.api.IntEnum;
import org.comroid.api.Named;
import org.comroid.api.Rewrapper;
import org.comroid.common.info.Described;

public enum VerificationLevel implements IntEnum, Named, Described {
    NONE(0, "unrestricted"),
    LOW(1, "must have verified email on account"),
    MEDIUM(2, "must be registered on Discord for longer than 5 minutes"),
    HIGH(3, "must be a member of the server for longer than 10 minutes"),
    VERY_HIGH(4, "must have a verified phone number");

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

    @Override
    public String getAlternateFormattedName() {
        return getDescription();
    }

    VerificationLevel(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public static Rewrapper<VerificationLevel> valueOf(int value) {
        return IntEnum.valueOf(value, VerificationLevel.class);
    }
}
