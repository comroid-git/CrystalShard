package de.kaleidox.crystalshard.main.items.server;

import java.util.stream.Stream;

public enum ExplicitContentFilterLevel {
    UNKNOWN(-1),
    DISABLED(0),
    MEMBERS_WITHOUT_ROLES(1),
    ALL_MEMBERS(2);
    private final int id;

    ExplicitContentFilterLevel(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    // Static members
    // Static membe
    public static ExplicitContentFilterLevel getFromId(int id) {
        return Stream.of(values())
                .filter(level -> level.id == id)
                .findAny()
                .orElse(UNKNOWN);
    }
}
