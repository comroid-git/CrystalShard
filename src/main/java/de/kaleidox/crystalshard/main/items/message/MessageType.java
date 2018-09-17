package de.kaleidox.crystalshard.main.items.message;

import java.util.stream.Stream;

@SuppressWarnings("unused")
public enum MessageType {
    UNKNOWN(-1),
    DEFAULT(0),
    RECIPIENT_ADD(1),
    RECIPIENT_REMOVE(2),
    CALL(3),
    CHANNEL_NAME_CHANGE(4),
    CHANNEL_ICON_CHANGE(5),
    CHANNEL_PINNED_MESSAGE(6),
    GUILD_MEMBER_JOIN(7);
    private final int id;
    
    MessageType(int id) {
        this.id = id;
    }
    
// Static membe
    public static MessageType getTypeById(int id) {
        return Stream.of(values()).filter(type -> type.id == id).findAny().orElse(UNKNOWN);
    }
}
