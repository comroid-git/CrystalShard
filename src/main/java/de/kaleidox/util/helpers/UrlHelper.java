package de.kaleidox.util.helpers;

import de.kaleidox.logging.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

public class UrlHelper {
    public final static String BASE_IMAGE_URL = "https://cdn.discordapp.com/";
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
                return imageUrl(null, of); // try create a discord image url
            }
        }
        return null;
    }

    public static URL imageUrl(ImageEndpoint imageEndpoint, String of) {
        if (Objects.nonNull(of)) {
            try {
                return new URL(of);
            } catch (MalformedURLException e) {
                logger.exception(e);
                return null;
            }
        }
        return null;
    }

    public static URL ignoreIfNull(String of) {
        if (Objects.nonNull(of)) {
            try {
                return new URL(of);
            } catch (MalformedURLException e) {
                return null;
            }
        }
        return null;
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
