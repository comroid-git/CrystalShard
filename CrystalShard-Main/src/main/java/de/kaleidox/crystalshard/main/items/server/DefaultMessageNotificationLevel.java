package de.kaleidox.crystalshard.main.items.server;

import java.util.stream.Stream;

public enum DefaultMessageNotificationLevel {
    UNKNOWN(-1),
    ALL_MESSAGES(0),
    ONLY_MENTIONS(1);
    private final int id;
    
    DefaultMessageNotificationLevel(int id) {
        this.id = id;
    }
    
    public int getId() {
        return id;
    }
    
    // Static members
    // Static membe
    public static DefaultMessageNotificationLevel getFromId(int id) {
        return Stream.of(values())
                .filter(level -> level.id == id)
                .findAny()
                .orElse(UNKNOWN);
    }
}
