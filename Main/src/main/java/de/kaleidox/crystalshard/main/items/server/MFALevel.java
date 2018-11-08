package de.kaleidox.crystalshard.main.items.server;

import java.util.stream.Stream;

public enum MFALevel {
    UNKNOWN(-1),
    DEACTIVATED(0),
    ACTIVATED(1);
    private final int id;

    MFALevel(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    // Static members
    // Static membe
    public static MFALevel getFromId(int id) {
        return Stream.of(values())
                .filter(level -> level.id == id)
                .findAny()
                .orElse(UNKNOWN);
    }
}
