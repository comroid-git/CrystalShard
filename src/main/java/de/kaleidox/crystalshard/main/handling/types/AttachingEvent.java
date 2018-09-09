package de.kaleidox.crystalshard.main.handling.types;

import de.kaleidox.crystalshard.main.util.Castable;

import java.util.NoSuchElementException;
import java.util.stream.Stream;

public interface AttachingEvent extends Castable<AttachingEvent> {
    Class<? super AttachingEvent>[] superClasses = new Class[]{
            ChannelAttachingEvent.class,
            MessageAttachingEvent.class
    };

    default boolean test(AttachingEvent other) {
        return this == other;
    }

    static <T extends AttachingEvent> T getFromToken(String token, Class<T> getAs) throws NoSuchElementException {
        return Stream.of(superClasses)
                .flatMap(klass -> Stream.of(klass.getEnumConstants()))
                .filter(element -> ((Enum) element).name().equalsIgnoreCase(token))
                .map(getAs::cast)
                .findAny()
                .orElseThrow(NoSuchElementException::new);
    }
}
