package org.comroid.crystalshard.api.model.message.embed;

import java.awt.Color;
import java.net.URL;
import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

import org.comroid.crystalshard.adapter.Adapter;
import org.comroid.crystalshard.adapter.MainAPI;

@MainAPI
public interface Embed extends EmbedModificator<Embed> {
    Optional<String> getTitle();

    Optional<String> getDescription();

    Optional<URL> getURL();

    Optional<Instant> getTimestamp();

    Optional<Color> getColor();

    Optional<? extends Footer> getFooter();

    Optional<? extends Image> getImage();

    Optional<? extends Thumbnail> getThumbnail();

    Optional<? extends Video> getVideo();

    Optional<? extends Provider> getProvider();

    Optional<? extends Author> getAuthor();

    Collection<? extends Field> getFields();

    static Composer composer() {
        return Adapter.require(Composer.class);
    }

    @MainAPI
    interface Footer {
        String getText();

        Optional<URL> getIconURL();

        Optional<URL> getProxyIconURL();
    }

    @MainAPI
    interface Image {
        Optional<URL> getURL();

        Optional<URL> getProxyURL();

        Optional<Integer> getHeight();

        Optional<Integer> getWidth();
    }

    @MainAPI
    interface Thumbnail {
        Optional<URL> getURL();

        Optional<URL> getProxyURL();

        Optional<Integer> getHeight();

        Optional<Integer> getWidth();
    }

    @MainAPI
    interface Video {
        Optional<URL> getURL();

        Optional<Integer> getHeight();

        Optional<Integer> getWidth();
    }

    @MainAPI
    interface Provider {
        Optional<String> getName();

        Optional<URL> getURL();
    }

    @MainAPI
    interface Author {
        Optional<String> getName();

        Optional<URL> getURL();

        Optional<URL> getIconURL();

        Optional<URL> getProxyIconURL();
    }

    @MainAPI
    interface Field {
        String getName();

        String getValue();

        boolean isInline();

        static Field of(String title, String text, boolean inline) {
            return Adapter.require(Embed.Field.class, title, text, inline);
        }
    }

    interface Composer extends EmbedModificator<Composer> { // TODO: 14.11.2019  
        Embed compose();
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
