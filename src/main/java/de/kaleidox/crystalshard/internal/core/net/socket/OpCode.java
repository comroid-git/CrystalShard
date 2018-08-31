package de.kaleidox.crystalshard.internal.core.net.socket;

import java.util.Optional;
import java.util.stream.Stream;

public enum OpCode {
    /**
     * Dispatches an listener.
     */
    DISPATCH(0),

    /**
     * Used for ping checking.
     */
    HEARTBEAT(1),

    /**
     * Used for client handshake.
     */
    IDENTIFY(2),

    /**
     * Used to update the client status.
     */
    STATUS_UPDATE(3),

    /**
     * Used to join/move/leave voice channels.
     */
    VOICE_STATE_UPDATE(4),

    /**
     * Used for voice ping checking.
     */
    VOICE_SERVER_PING(5),

    /**
     * Used to resume a closed connection.
     */
    RESUME(6),

    /**
     * Used to tell clients to reconnect to the gateway.
     */
    RECONNECT(7),

    /**
     * Used to request guild members.
     */
    REQUEST_GUILD_MEMBERS(8),

    /**
     * Used to notify client they have an invalid session id.
     */
    INVALID_SESSION(9),

    /**
     * Sent immediately after connecting, contains heartbeat and server debug information.
     */
    HELLO(10),

    /**
     * Sent immediately following a client heartbeat that was received.
     */
    HEARTBEAT_ACK(11);

    /**
     * The actual numeric code.
     */
    private final int code;

    /**
     * Creates a new gateway opcode.
     *
     * @param code The actual numeric code.
     */
    OpCode(int code) {
        this.code = code;
    }

    /**
     * Gets the actual numeric code.
     *
     * @return The actual numeric code.
     */
    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return this.name() + "(" + code + ")";
    }

    public static Optional<OpCode> getByCode(int code) {
        return Stream.of(values())
                .filter(opCode -> opCode.code == code)
                .findAny();
    }
}