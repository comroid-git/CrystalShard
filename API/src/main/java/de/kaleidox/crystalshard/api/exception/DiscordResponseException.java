package de.kaleidox.crystalshard.api.exception;

public class DiscordResponseException extends RuntimeException {
    public DiscordResponseException(String message) {
        super(message);
    }
}
