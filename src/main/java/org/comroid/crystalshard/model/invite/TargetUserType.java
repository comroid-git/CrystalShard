package org.comroid.crystalshard.model.invite;

import org.comroid.api.IntegerAttribute;
import org.comroid.api.Rewrapper;
import org.jetbrains.annotations.NotNull;

public enum TargetUserType implements IntegerAttribute {
    STREAM(1);

    private final int value;

    @Override
    public @NotNull Integer getValue() {
        return value;
    }

    TargetUserType(int value) {
        this.value = value;
    }

    public static Rewrapper<TargetUserType> valueOf(int value) {
        return IntegerAttribute.valueOf(value, TargetUserType.class);
    }
}
