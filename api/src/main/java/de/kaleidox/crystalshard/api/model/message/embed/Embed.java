package de.kaleidox.crystalshard.api.model.message.embed;

import java.awt.Color;
import java.net.URL;
import java.time.Instant;
import java.util.Collection;
import java.util.Optional;
import java.util.OptionalInt;

import de.kaleidox.crystalshard.util.model.serialization.JsonDeserializable;

public interface Embed extends JsonDeserializable { // todo serialize
    Optional<String> getTitle();

    Optional<String> getDescription();

    Optional<URL> getURL();

    Optional<Instant> getTimestamp();

    Optional<Color> getColor();

    Optional<Footer> getFooter();

    Optional<Image> getImage();

    Optional<Thumbnail> getThumbnail();

    Optional<Video> getVideo();

    Optional<Provider> getProvider();

    Optional<Author> getAuthor();

    Collection<Field> getFields();

    interface Footer {
        String getText();

        Optional<URL> getIconURL();

        Optional<URL> getProxyIconURL();
    }

    interface Image {
        Optional<URL> getURL();

        Optional<URL> getProxyURL();

        OptionalInt getHeight();

        OptionalInt getWidth();
    }

    interface Thumbnail {
        Optional<URL> getURL();

        Optional<URL> getProxyURL();

        OptionalInt getHeight();

        OptionalInt getWidth();
    }

    interface Video {
        Optional<URL> getURL();

        OptionalInt getHeight();

        OptionalInt getWidth();
    }

    interface Provider {
        Optional<String> getName();

        Optional<URL> getURL();
    }

    interface Author {
        Optional<String> getName();

        Optional<URL> getURL();

        Optional<URL> getIconURL();

        Optional<URL> getProxyIconURL();
    }

    interface Field {
        String getName();

        String getValue();

        boolean isInline();
    }

    interface Composer {
    }

    final class Limit {
        public static final int TITLE_LENGTH = 256;

        public static final int DESCRIPTION_LENGTH = 2048;

        public static final int FIELD_COUNT = 25;

        public static final int FIELD_TITLE_LENGTH = 256;

        public static final int FIELD_TEXT_LENGTH = 1024;

        public static final int FOOTER_TEXT_LENGTH = 2048;

        public static final int AUTHOR_NAME_LENGTH = 256;

        public static final int TOTAL_CHARACTERS = 6000;
    }
}
