package de.kaleidox.crystalshard.main.items.server;

import java.util.stream.Stream;

public enum VerificationLevel {
    UNKNOWN(-1, "unknown"),
    NONE(0, "unrestricted"),
    LOW(1, "must have verified email on account"),
    MEDIUM(2, "must be registered on Discord for longer than 5 minutes"),
    HIGH(3, "(╯°□°）╯︵ ┻━┻ - must be a member of the server for longer than 10 minutes"),
    VERY_HIGH(4, "┻━┻ミヽ(ಠ益ಠ)ﾉ彡┻━┻ - must have a verified phone number");
    private final int id;
    private final String description;

    VerificationLevel(int id, String description) {
        this.id = id;
        this.description = description;
    }

    // Static members
    // Static membe
    public static VerificationLevel getFromId(int id) {
        return Stream.of(values())
                .filter(level -> level.id == id)
                .findAny()
                .orElse(UNKNOWN);
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}
