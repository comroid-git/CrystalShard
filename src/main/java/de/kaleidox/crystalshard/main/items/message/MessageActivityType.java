package de.kaleidox.crystalshard.main.items.message;

import java.util.stream.Stream;

@SuppressWarnings("unused")
public enum MessageActivityType {
    UNKNOWN(-1),
    JOIN(1),
    SPECTATE(2),
    LISTEN(3),
    JOIN_REQUEST(5);
    private final int id;
    
    MessageActivityType(int id) {
        this.id = id;
    }
    
    public int getId() {
        return id;
    }
    
// Static membe
    public static MessageActivityType getById(int id) {
        return Stream.of(values()).filter(type -> type.id == id).findAny().orElse(UNKNOWN);
    }
}
