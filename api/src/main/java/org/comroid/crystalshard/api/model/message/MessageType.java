package org.comroid.crystalshard.api.model.message;

import org.jetbrains.annotations.Nullable;

public enum MessageType {
    DEFAULT(0),

    RECIPIENT_ADD(1),

    RECIPIENT_REMOVE(2),

    CALL(3),

    CHANNEL_NAME_CHANGE(4),

    CHANNEL_ICON_CHANGE(5),

    CHANNEL_PINNED_MESSAGE(6),

    GUILD_MEMBER_JOIN(7),

    USER_PREMIUM_GUILD_SUBSCRIPTION(8),

    USER_PREMIUM_GUILD_SUBSCRIPTION_TIER_1(9),

    USER_PREMIUM_GUILD_SUBSCRIPTION_TIER_2(10),

    USER_PREMIUM_GUILD_SUBSCRIPTION_TIER_3(11),

    CHANNEL_FOLLOW_ADD(12);

    public final int value;

    MessageType(int value) {
        this.value = value;
    }

    public static @Nullable MessageType valueOf(int value) {
        for (MessageType type : values())
            if (type.value == value)
                return type;
            
        return null;
    }
}
