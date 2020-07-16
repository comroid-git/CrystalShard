package org.comroid.crystalshard.core.gateway;

import org.comroid.restless.socket.model.OPCode;
import org.comroid.restless.socket.model.OpCodeUsage;

public enum GatewayOPCode implements OPCode {
    DISPATCH(0, OpCodeUsage.RECEIVE),
    HEARTBEAT(1, OpCodeUsage.BIDIRECTIONAL),
    IDENTIFY(2, OpCodeUsage.SEND),
    PRESENCE_UPDATE(3, OpCodeUsage.SEND),
    VOICE_STATE_UPDATE(4, OpCodeUsage.SEND),
    RESUME(6, OpCodeUsage.SEND),
    RECONNECT(7, OpCodeUsage.RECEIVE),
    REQUEST_GUILD_MEMBERS(8, OpCodeUsage.SEND),
    INVALID_SESSION(9, OpCodeUsage.RECEIVE),
    HELLO(10, OpCodeUsage.RECEIVE),
    HEARTBEAT_ACK(11, OpCodeUsage.RECEIVE);

    private final int value;
    private final OpCodeUsage opCodeUsage;

    @Override
    public int getValue() {
        return value;
    }

    public OpCodeUsage getUsage() {
        return opCodeUsage;
    }

    GatewayOPCode(int value, OpCodeUsage opCodeUsage) {
        this.value = value;
        this.opCodeUsage = opCodeUsage;
    }

    public static GatewayOPCode valueOf(int value) {
        for (GatewayOPCode opCode : values())
            if (opCode.value == value)
                return opCode;
        return null;
    }
}
