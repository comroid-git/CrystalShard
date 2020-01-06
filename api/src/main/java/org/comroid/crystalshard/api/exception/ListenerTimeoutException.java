package org.comroid.crystalshard.api.exception;

public class ListenerTimeoutException extends RuntimeException {
    public ListenerTimeoutException() {
        super("Listener timed out");
    }

    public ListenerTimeoutException(String message) {
        super(message);
    }
}
