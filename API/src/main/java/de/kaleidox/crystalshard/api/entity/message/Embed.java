package de.kaleidox.crystalshard.api.entity.message;

import java.awt.Color;
import java.net.URI;
import java.net.URL;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import de.kaleidox.crystalshard.api.util.Castable;

import com.google.inject.Guice;

@SuppressWarnings("unused")
public interface Embed extends Castable<Embed> {
    Optional<Message> getMessage();

    Optional<String> getTitle();

    Optional<String> getDescription();

    Optional<URL> getUrl();

    Optional<Instant> getTimestamp();

    Optional<Color> getColor();

    Optional<Author> getAuthor();

    Optional<Thumbnail> getThumbnail();

    List<Field> getFields();

    Optional<Image> getImage();

    Optional<Video> getVideo();

    Optional<Footer> getFooter();

    Optional<Provider> getProvider();

    State getState();

    Updater updater();

    static Builder builder() {
        Guice.
    }

    enum State {
        UNSENT,
        SENT
    }

    // Partial Members
    interface Member {
        Embed getEmbed();

        State getState();
    }

    interface AttachmentMember extends Member {
        boolean hasAttachment();
    }

    // Full Members
    interface Author extends Member {
        String getName();

        Optional<URL> getUrl();

        Optional<URI> getIconUri();

        Optional<URL> getProxyIconUrl();

        Updater updater();

        static Builder builder() {
            return InternalInjector.newInstance(Embed.Author.Builder.class);
        }

        interface Builder {
            Builder setName(String name) throws IllegalArgumentException;

            Builder setUrl(URL url);

            Builder setIconUrl(URL url) throws AssertionError;

            Author build() throws IllegalStateException;
        }

        interface Updater extends Builder {
            @Override
            Updater setName(String name) throws IllegalArgumentException;

            @Override
            Updater setUrl(URL url);

            @Override
            Updater setIconUrl(URL url) throws AssertionError;

            CompletableFuture<Message> update() throws IllegalStateException;
        }
    }

    interface Thumbnail extends Member {
        URI getImageUri();

        Optional<URL> getProxyImageUrl();

        Updater updater();

        static Builder builder() {
            return InternalInjector.newInstance(Embed.Thumbnail.Builder.class);
        }

        interface Builder {
            Builder setImageUrl(URL url);

            Thumbnail build() throws IllegalStateException;
        }

        interface Updater extends Builder {
            @Override
            Updater setImageUrl(URL url);

            CompletableFuture<Message> update() throws IllegalStateException;
        }
    }

    interface Field extends Member {
        String getTitle();

        String getContent();

        boolean isInline();

        Updater updater();

        static Builder builder() {
            return InternalInjector.newInstance(Embed.Field.Builder.class);
        }

        interface Builder {
            Builder setTitle(String title) throws IllegalArgumentException;

            Builder setContent(String content) throws IllegalArgumentException;

            Builder setInline(boolean inline);

            Field build() throws IllegalStateException;
        }

        interface Updater extends Builder {
            @Override
            Updater setTitle(String title) throws IllegalArgumentException;

            @Override
            Updater setContent(String content) throws IllegalArgumentException;

            @Override
            Updater setInline(boolean inline);

            CompletableFuture<Message> update() throws IllegalStateException;
        }
    }

    interface Image extends Member {
        URI getImageUri();

        Optional<URL> getProxyImageUrl();

        Updater updater();

        static Builder builder() {
            return InternalInjector.newInstance(Embed.Image.Builder.class);
        }

        interface Builder {
            Builder setImageUrl(URL url);

            Image build() throws IllegalStateException;
        }

        interface Updater extends Builder {
            @Override
            Updater setImageUrl(URL url);

            CompletableFuture<Message> update() throws IllegalStateException;
        }
    }

    interface Video extends Member {
        URL getVideoUrl();

        @Override
        default State getState() {
            return getEmbed().getState();
        }
    }

    interface Footer extends Member {
        String getText();

        Optional<URL> getUrl();

        Optional<URI> getIconUri();

        Optional<URL> getProxyIconUrl();

        Updater updater();

        static Builder builder() {
            return InternalInjector.newInstance(Embed.Footer.Builder.class);
        }

        interface Builder {
            Builder setText(String text) throws IllegalArgumentException;

            Builder setUrl(URL url);

            Footer build() throws IllegalStateException;
        }

        interface Updater extends Builder {
            @Override
            Updater setText(String text) throws IllegalArgumentException;

            @Override
            Updater setUrl(URL url);

            CompletableFuture<Message> update() throws IllegalStateException;
        }
    }

    interface Provider extends Member {
        String getName();

        URL getUrl();

        @Override
        default State getState() {
            return State.SENT;
        }
    }

    interface Builder {
        Builder setTitle(String title) throws IllegalArgumentException, IllegalStateException;

        Builder setDescription(String description) throws IllegalArgumentException, IllegalStateException;

        Builder setUrl(URL url) throws IllegalStateException;

        Builder setTimestamp(Instant instant) throws IllegalStateException;

        Builder setColor(Color color) throws IllegalStateException;

        Builder setAuthor(Author author) throws IllegalStateException;

        Builder setThumbnail(Thumbnail thumbnail) throws IllegalStateException;

        Builder setImage(Image image) throws IllegalStateException;

        Builder setFooter(Footer footer) throws IllegalStateException;

        Builder addField(Field field) throws IllegalStateException;

        Embed build() throws IllegalStateException;
    }

    interface Updater extends Builder {
        @Override
        Updater setTitle(String title) throws IllegalArgumentException, IllegalStateException;

        @Override
        Updater setDescription(String description) throws IllegalArgumentException, IllegalStateException;

        @Override
        Updater setUrl(URL url) throws IllegalStateException;

        @Override
        Updater setTimestamp(Instant instant) throws IllegalStateException;

        @Override
        Updater setColor(Color color) throws IllegalStateException;

        @Override
        Updater setAuthor(Author author) throws IllegalStateException;

        @Override
        Updater setThumbnail(Thumbnail thumbnail) throws IllegalStateException;

        @Override
        Updater setImage(Image image) throws IllegalStateException;

        @Override
        Updater setFooter(Footer footer) throws IllegalStateException;

        @Override
        Updater addField(Field field) throws IllegalStateException;

        CompletableFuture<Message> update() throws IllegalStateException;
    }

    interface Boundaries {
        int TITLE_LENGTH = 256;
        int DESCRIPTION_LENGTH = 2048;
        int FIELD_COUNT = 25;
        int FIELD_TITLE_LENGTH = 256;
        int FIELD_TEXT_LENGTH = 1024;
        int FOOTER_LENGTH = 2048;
        int AUTHOR_NAME_LENGTH = 256;
        int TOTAL_CHAR_COUNT = 6000;
    }
}
