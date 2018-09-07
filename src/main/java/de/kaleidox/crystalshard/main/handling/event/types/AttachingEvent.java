package de.kaleidox.crystalshard.main.handling.event.types;

public interface AttachingEvent {
    default boolean test(AttachingEvent other) {
        return this == other;
    }
}
