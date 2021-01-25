package org.comroid.crystalshard.ui;

import org.comroid.api.IntEnum;
import org.comroid.common.info.Described;
import org.jetbrains.annotations.NotNull;

public enum InteractionResponseType implements IntEnum, Described {
    PONG(1, "ACK a Ping"),
    ACKNOWLEDGE(2, "ACK a command without sending a message, eating the user's input"),
    CHANNEL_MESSAGE(3, "respond with a message, eating the user's input"),
    CHANNEL_MESSAGE_WITH_SOURCE(4, "respond with a message, showing the user's input"),
    ACKNOWLEDGE_WITH_SOURCE(5, "ACK a command without sending a message, showing the user's input");

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

    InteractionResponseType(int value, String description) {
        this.value = value;
        this.description = description;
    }
}
