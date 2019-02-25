package de.kaleidox.crystalshard.api.exception;

public class UncachedItemException extends Throwable {
    public UncachedItemException(String message) {
        super(message);
    }

    public UncachedItemException() {
        super("The item is not cached.");
    }

    public UncachedItemException(String s, boolean lowStackTrace) {
        super(s);
    }
}
