package de.kaleidox.crystalshard.main.exception;

public class UncachedItemException extends Throwable implements LowStackTraceable {
    private final boolean lowStackTrace;

    public UncachedItemException(String message) {
        super(message);
        lowStackTrace = false;
    }

    public UncachedItemException() {
        super("The item is not cached.");
        lowStackTrace = false;
    }

    public UncachedItemException(String s, boolean lowStackTrace) {
        super(s);
        this.lowStackTrace = lowStackTrace;
    }

    // Override Methods
    @Override
    public boolean lowStackTrace() {
        return lowStackTrace;
    }
}
