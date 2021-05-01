package org.comroid.crystalshard.model.guild;

import org.comroid.api.IntegerAttribute;
import org.comroid.api.Named;
import org.comroid.api.Rewrapper;
import org.jetbrains.annotations.NotNull;

public enum ExplicitContentFilter implements IntegerAttribute, Named {
    DISABLED(0),
    MEMBERS_WITHOUT_ROLES(1),
    ALL_MEMBERS(2);

    private final int value;

    @Override
    public @NotNull Integer getValue() {
        return value;
    }

    ExplicitContentFilter(int value) {
        this.value = value;
    }

    public static Rewrapper<ExplicitContentFilter> valueOf(int value) {
        return IntegerAttribute.valueOf(value, ExplicitContentFilter.class);
    }
}
