package org.comroid.crystalshard.model.guild;

import org.comroid.api.BitmaskEnum;
import org.comroid.api.Named;
import org.comroid.common.info.Described;

import java.util.Set;

@SuppressWarnings("PointlessBitwiseExpression")
public enum SystemChannelFlag implements BitmaskEnum<SystemChannelFlag>, Named, Described {
    SUPPRESS_JOIN_NOTIFICATIONS(1 << 0,"Suppress member join notifications"),
    SUPPRESS_PREMIUM_SUBSCRIPTIONS(1 << 1, "Suppress server boost notifications");

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

    SystemChannelFlag(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public static Set<SystemChannelFlag> valueOf(int mask) {
        return BitmaskEnum.valueOf(mask, SystemChannelFlag.class);
    }
}
