package de.kaleidox.crystalshard.main.exception;

public class UncachedItemException extends Throwable {
    public UncachedItemException(String message) {
        super(message);
    }

    public UncachedItemException() {
        super("The item is not cached.");
    }
}
