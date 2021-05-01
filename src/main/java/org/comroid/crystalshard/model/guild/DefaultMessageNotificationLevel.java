package org.comroid.crystalshard.model.guild;

import org.comroid.api.IntegerAttribute;
import org.comroid.api.Named;
import org.comroid.api.Rewrapper;
import org.jetbrains.annotations.NotNull;

public enum DefaultMessageNotificationLevel implements IntegerAttribute, Named {
    ALL_MESSAGES(0),
    ONLY_MENTIONS(1);

    private final int value;

    @Override
    public @NotNull Integer getValue() {
        return value;
    }

    DefaultMessageNotificationLevel(int value) {
        this.value = value;
    }

    public static Rewrapper<DefaultMessageNotificationLevel> valueOf(int value) {
        return IntegerAttribute.valueOf(value, DefaultMessageNotificationLevel.class);
    }
}
