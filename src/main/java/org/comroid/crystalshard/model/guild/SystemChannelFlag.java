package org.comroid.crystalshard.model.guild;

import org.comroid.api.BitmaskAttribute;
import org.comroid.api.Named;
import org.comroid.common.info.Described;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

@SuppressWarnings("PointlessBitwiseExpression")
public enum SystemChannelFlag implements BitmaskAttribute<SystemChannelFlag>, Named, Described {
    SUPPRESS_JOIN_NOTIFICATIONS(1 << 0,"Suppress member join notifications"),
    SUPPRESS_PREMIUM_SUBSCRIPTIONS(1 << 1, "Suppress server boost notifications");

    private final int value;
    private final String description;

    @Override
    public @NotNull Integer getValue() {
        return value;
    }

    @Override
    public String getDescription() {
        return description;
    }

    SystemChannelFlag(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public static Set<SystemChannelFlag> valueOf(int mask) {
        return BitmaskAttribute.valueOf(mask, SystemChannelFlag.class);
    }
}
