package org.comroid.crystalshard.model.message;

import org.comroid.common.info.Described;
import org.comroid.common.util.Bitmask;

import java.util.Set;

public enum MessageFlag implements Bitmask.Enum<MessageFlag>, Described {
    CROSSPOSTED(1, "this message has been published to subscribed channels (via Channel Following)"),
    IS_CROSSPOSTED(1 << 1, "this message originated from a message in another channel (via Channel Following)"),
    SUPPRESS_EMBEDS(1 << 2, "do not include any embeds when serializing this message"),
    SOURCE_MESSAGE_DELETED(1 << 3, "the source message for this crosspost has been deleted (via Channel Following)"),
    URGENT(1 << 4, "this message came from the urgent message system");

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

    MessageFlag(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public static Set<MessageFlag> valueOf(int mask) {
        return Bitmask.Enum.valueOf(mask, MessageFlag.class);
    }
}
