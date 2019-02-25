package de.kaleidox.crystalshard.logging;

/**
 * A functional interface to consume exceptions yourself in the Logger. Every logged exception gets forwarded to this handler <i>before log post</i>.
 *
 * @see Logger#registerCustomExceptionHandler(CustomExceptionHandler)
 */
@FunctionalInterface
public interface CustomHandler {
    /**
     * The method that gets called.
     *
     * @param level   The logging level of the message.
     * @param message The message that gets logged.
     */
    void apply(LoggingLevel level, String message);
}
