package de.kaleidox.crystalshard.main.event.types;

public interface AttachingEvent {
    default boolean test(AttachingEvent other) {
        return this == other;
    }
}
