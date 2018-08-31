package de.kaleidox.util;

import de.kaleidox.logging.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

public class UrlHelper {
    private final static Logger logger = new Logger(UrlHelper.class);

    /**
     * Creates an URL from the given String.
     * Returns {@code null} if the given String is null.
     * Returns {@code null} and logs a {@link MalformedURLException} if the URL is malformed.
     *
     * @param of The string to create an URL from.
     * @return An URL or {@code null}.
     */
    public static URL orNull(String of) {
        if (Objects.nonNull(of)) {
            try {
                return new URL(of);
            } catch (MalformedURLException e) {
                logger.exception(e);
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Creates an URL from the given String that must not be null.
     * Returns {@code null} and logs a {@link MalformedURLException} if the URL is malformed.
     *
     * @param of The string to create an URL from.
     * @return An URL or {@code null}.
     */
    public static URL require(String of) {
        Objects.requireNonNull(of);
        try {
            return new URL(of);
        } catch (MalformedURLException e) {
            logger.exception(e);
            throw new IllegalArgumentException("MalformedURLException: " + e.getMessage());
        }
    }
}
