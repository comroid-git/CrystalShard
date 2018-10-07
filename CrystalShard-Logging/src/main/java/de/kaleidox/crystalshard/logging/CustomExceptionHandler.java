package de.kaleidox.crystalshard.logging;

/**
 * A functional interface to consume exceptions yourself in the Logger. Every logged exception gets forwarded to this
 * handler <i>before log post</i>.
 *
 * @see Logger#registerCustomExceptionHandler(CustomExceptionHandler)
 */
@FunctionalInterface
public interface CustomExceptionHandler {
    /**
     * The method that gets called.
     *
     * @param throwable The throwable.
     */
    void apply(Throwable throwable);
}
