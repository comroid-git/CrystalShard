package de.comroid.crystalshard.core.gateway;

import java.lang.reflect.Field;

import org.jetbrains.annotations.Nullable;

public final class GatewayStatusCodes {
    // 4xxx - Close Event
    /**
     * We're not sure what went wrong. Try reconnecting?
     */
    public static final int UNKNOWN_ERROR = 4000;

    /**
     * You sent an invalid Gateway opcode or an invalid payload for an opcode. Don't do that!
     */
    public static final int UNKNOWN_OPCODE = 4001;

    /**
     * You sent an invalid payload to us. Don't do that!
     */
    public static final int DECODE_ERROR = 4002;

    /**
     * You sent us a payload prior to identifying.
     */
    public static final int NOT_AUTHENTICATED = 4003;

    /**
     * The account token sent with your identify payload is incorrect.
     */
    public static final int AUTHENTICATION_FAILED = 4004;

    /**
     * You sent more than one identify payload. Don't do that!
     */
    public static final int ALREADY_AUTHENTICATED = 4005;

    /**
     * The sequence sent when resuming the session was invalid. Reconnect and start a new session.
     */
    public static final int INVALID_SEQ = 4007;

    /**
     * Woah nelly! You're sending payloads to us too quickly. Slow it down!
     */
    public static final int RATE_LIMITED = 4008;

    /**
     * Your session timed out. Reconnect and start a new one.
     */
    public static final int SESSION_TIMEOUT = 4009;

    /**
     * You sent us an invalid shard when identifying.
     */
    public static final int INVALID_SHARD = 4010;

    /**
     * The session would have handled too many guilds - you are required to shard your connection in order to connect.
     */
    public static final int SHARDING_REQUIRED = 4011;

    private static final Field[] fields = GatewayStatusCodes.class.getFields();

    private GatewayStatusCodes() {
        // nope
    }

    public static @Nullable String toString(int code) {
        try {
            for (Field field : fields)
                if (((int) field.get(null)) == code)
                    return field.getName() + "(" + code + ")";
        } catch (Throwable e) {
            throw new AssertionError("Unexpected Exception", e);
        }

        return null;
    }
}
