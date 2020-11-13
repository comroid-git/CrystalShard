package org.comroid.crystalshard.core.gateway;

import java.util.Optional;

import org.comroid.common.ref.IntEnum;

public enum CloseCode implements IntEnum {
    UNKNOWN(4000, "We're not sure what went wrong. Try reconnecting?"),
    UNKNOWN_OPCODE(4001, "You sent an invalid Gateway opcode or an invalid payload for an opcode. Don't do that!"),
    DECODE_ERROR(4002, "You sent an invalid payload to us. Don't do that!"),
    NOT_AUTHENTICATED(4003, "You sent us a payload prior to identifying."),
    AUTHENTICATION_FAILED(4004, "The account token sent with your identify payload is incorrect."),
    ALREADY_AUTHENTICATED(4005, "You sent more than one identify payload. Don't do that!"),
    INVALID_SEQ(4007, "The sequence sent when resuming the session was invalid. Reconnect and start a new session."),
    RATELIMITED(
            4008,
            "Woah nelly! You're sending payloads to us too quickly. Slow it down! You will be disconnected on receiving this."
    ),
    SESSION_TIMED_OUT(4009, "Your session timed out. Reconnect and start a new one."),
    INVALID_SHARD(4010, "You sent us an invalid shard when identifying."),
    SHARDING_REQUIRED(
            4011,
            "The session would have handled too many guilds - you are required to shard your connection in order to connect."
    ),
    INVALID_API_VERSION(4012, "You sent an invalid version for the gateway."),
    INVALID_INTENT(4013,
            "You sent an invalid intent for a Gateway Intent. You may have incorrectly calculated the bitwise value."
    ),
    DISALLOWED_INTENT(4014,
            "You sent a disallowed intent for a Gateway Intent. You may have tried to specify an intent that you have not " +
                    "enabled or are not whitelisted for."
    );

    private final int code;
    private final String description;

    @Override
    public int getValue() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    CloseCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static String toString(int code) {
        return valueOf(code).toString();
    }

    public static Optional<CloseCode> valueOf(int code) {
        for (CloseCode each : values()) {
            if (each.code == code) {
                return Optional.of(each);
            }
        }

        return Optional.empty();
    }

    public void fail() throws RuntimeException {
        throw new RuntimeException(description);
    }

    public void fail(String messageDetail) throws RuntimeException {
        throw new RuntimeException(description + " " + messageDetail);
    }

    @Override
    public String toString() {
        return String.format("GatewayCloseCode{%d: %s}", code, description);
    }
}
