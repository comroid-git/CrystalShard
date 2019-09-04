package de.kaleidox.crystalshard.api.model.message;

// https://discordapp.com/developers/docs/resources/channel#message-object-message-flags

@SuppressWarnings("PointlessBitwiseExpression")
public final class MessageFlags {
    public static final int CROSSPOSTED = 1 << 0;

    public static final int IS_CROSSPOST = 1 << 1;

    public static final int SUPPRESS_EMBEDS = 1 << 2;
}
