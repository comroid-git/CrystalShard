package org.comroid.crystalshard.core.net.gateway;

public enum OPCode implements org.comroid.restless.socket.model.OPCode {
    DISPATCH(0, Usage.RECEIVE),
    HEARTBEAT(1, Usage.BIDIRECTIONAL),
    IDENTIFY(2, Usage.SEND),
    PRESENCE_UPDATE(3, Usage.SEND),
    VOICE_STATE_UPDATE(4, Usage.SEND),
    RESUME(6, Usage.SEND),
    RECONNECT(7, Usage.RECEIVE),
    REQUEST_GUILD_MEMBERS(8, Usage.SEND),
    INVALID_SESSION(9, Usage.RECEIVE),
    HELLO(10, Usage.RECEIVE),
    HEARTBEAT_ACK(11, Usage.RECEIVE);

    private final int   value;
    private final Usage usage;

    OPCode(int value, Usage usage) {
        this.value = value;
        this.usage = usage;
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public Usage getUsage() {
        return usage;
    }
}
