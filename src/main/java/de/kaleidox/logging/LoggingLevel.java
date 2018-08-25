package de.kaleidox.logging;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * This enum represents different possible Logging levels.
 *
 * @see Logger
 */
public enum LoggingLevel {
    /**
     * A silent logger level.
     * Only used by {@link Logger#setLevel(LoggingLevel)}.
     */
    SILENT("", 0),

    /**
     * An error level to post exceptions and errors to.
     */
    ERROR("ERROR", 1),

    /**
     * An information level to post information to.
     */
    INFO("INFO", 2),

    /**
     * A warning level to post warnings to.
     */
    WARN("WARN", 3),

    /**
     * A debug level to post debug information to.
     */
    DEBUG("DEBUG", 4),

    /**
     * A tracing level to track down every net action.
     */
    TRACE("TRACE", 5),

    /**
     * A tracing level to track down every single sent action, things like sent image bytes.
     */
    DEEP_TRACE("DEEPTRACE", 6);

    private final String name;
    private int severity;

    LoggingLevel(String name, int ident) {
        this.name = name;
        this.severity = ident;
    }

    /**
     * Finds a logging level by its name.
     *
     * @param name The name to search for.
     * @return The logging level with that name.
     */
    public static Optional<LoggingLevel> ofName(String name) {
        return Stream.of(values())
                .filter(level -> level.name.equalsIgnoreCase(name))
                .findAny();
    }

    /**
     * Gets a severity integer of the level.
     *
     * @return The severity of the level.
     */
    public int getSeverity() {
        return severity;
    }

    /**
     * Gets the name of the level.
     *
     * @return The name of the level.
     */
    public String getName() {
        return name;
    }
}
