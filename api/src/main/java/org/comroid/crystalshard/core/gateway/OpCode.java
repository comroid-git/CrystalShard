package org.comroid.crystalshard.core.gateway;

import java.util.NoSuchElementException;

public enum OpCode {
    DISPATCH(0),

    HEARTBEAT(1),

    IDENTIFY(2),

    STATUS_UPDATE(3),

    VOICE_STATE_UPDATE(4),

    VOICE_SERVER_PING(5),

    RESUME(6),

    RECONNECT(7),

    REQUEST_GUILD_MEMBERS(8),

    INVALID_SESSION(9),

    HELLO(10),

    HEARTBEAT_ACK(11);

    public final int value;

    OpCode(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "OpCode(" + value + ")";
    }

    public static OpCode getByValue(int value) {
        for (OpCode opCode : values())
            if (opCode.value == value)
                return opCode;

        throw new NoSuchElementException("Unknown OpCode: " + value);
    }
}
