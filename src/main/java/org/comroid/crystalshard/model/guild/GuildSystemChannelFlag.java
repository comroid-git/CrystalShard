package org.comroid.crystalshard.model.guild;

import org.comroid.common.info.Described;
import org.comroid.common.util.Bitmask;

@SuppressWarnings("PointlessBitwiseExpression")
public enum GuildSystemChannelFlag implements Bitmask.Enum, Described {
    SUPPRESS_JOIN_NOTIFICATIONS(1 << 0, "Suppress member join notifications"),
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

    GuildSystemChannelFlag(int value, String description) {
        this.value = value;
        this.description = description;
    }
}
