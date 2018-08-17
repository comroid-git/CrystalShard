package de.kaleidox.crystalshard.internal.items.message.embed;

import de.kaleidox.crystalshard.main.items.message.embed.EmbedDraft;
import de.kaleidox.crystalshard.main.items.message.embed.SentEmbed;
import de.kaleidox.logging.Logger;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("ALL")
public class EmbedDraftInternal implements EmbedDraft {
    private final static Logger logger = new Logger(EmbedDraftInternal.class);
    private final String title;
    private final String description;
    private final URL url;
    private final Instant timestamp;
    private final Color color;
    private final EmbedDraft.Footer footer;
    private final EmbedDraft.Image image;
    private final EmbedDraft.Thumbnail thumbnail;
    private final EmbedDraft.Author author;
    private final ArrayList<EmbedDraft.Field> fields;

    public EmbedDraftInternal(String title,
                              String description,
                              URL url,
                              Instant timestamp,
                              Color color,
                              EmbedDraft.Footer footer,
                              EmbedDraft.Image image,
                              EmbedDraft.Thumbnail thumbnail,
                              EmbedDraft.Author author,
                              ArrayList<EmbedDraft.Field> fields) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.timestamp = timestamp;
        this.color = color;
        this.footer = footer;
        this.image = image;
        this.thumbnail = thumbnail;
        this.author = author;
        this.fields = fields;
    }

    @Override
    public Optional<EmbedDraft> toEmbedDraft() {
        return Optional.of(this);
    }

    @Override
    public Optional<SentEmbed> toSentEmbed() {
        return Optional.empty();
    }

    @Override
    public Optional<Builder> toBuilder() {
        // todo Craft a Builder
        return null;
    }

    @Override
    public Optional<String> getTitle() {
        return Optional.ofNullable(title);
    }

    @Override
    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

    @Override
    public Optional<URL> getUrl() {
        return Optional.ofNullable(url);
    }

    @Override
    public Optional<Instant> getTimestamp() {
        return Optional.ofNullable(timestamp);
    }

    @Override
    public Optional<Color> getColor() {
        return Optional.ofNullable(color);
    }

    @Override
    public Optional<EmbedDraft.Footer> getFooter() {
        return Optional.ofNullable(footer);
    }

    @Override
    public Optional<EmbedDraft.Image> getImage() {
        return Optional.ofNullable(image);
    }

    @Override
    public Optional<EmbedDraft.Thumbnail> getThumbnail() {
        return Optional.ofNullable(thumbnail);
    }

    @Override
    public Optional<EmbedDraft.Author> getAuthor() {
        return Optional.ofNullable(author);
    }

    @Override
    public List<EmbedDraft.Field> getFields() {
        return Collections.unmodifiableList(fields);
    }

    public static class Footer implements EmbedDraft.Footer {
        private final String name;
        private final URL url;

        public Footer(String name, String iconUrl) {
            this.name = name;
            URL tempUrl;
            try {
                tempUrl = new URL(iconUrl);
            } catch (MalformedURLException e) {
                logger.exception(e);
                tempUrl = null;
            }
            this.url = tempUrl;
        }

        public String getText() {
            return name;
        }

        public Optional<URL> getUrl() {
            return Optional.ofNullable(url);
        }
    }

    public static class Image implements EmbedDraft.Image {
        private final URL url;

        public Image(String url) {
            URL tempUrl;
            try {
                tempUrl = new URL(url);
            } catch (MalformedURLException e) {
                logger.exception(e);
                tempUrl = null;
            }
            this.url = tempUrl;
        }

        public Optional<URL> getUrl() {
            return Optional.ofNullable(url);
        }
    }

    public static class Thumbnail implements EmbedDraft.Thumbnail {
        private final URL url;

        public Thumbnail(String url) {
            URL tempUrl;
            try {
                tempUrl = new URL(url);
            } catch (MalformedURLException e) {
                logger.exception(e);
                tempUrl = null;
            }
            this.url = tempUrl;
        }

        public Optional<URL> getUrl() {
            return Optional.ofNullable(url);
        }
    }

    public static class Author implements EmbedDraft.Author {
        private final String name;
        private final String url;
        private final String iconUrl;

        public Author(String name, String url, String iconUrl) {
            this.name = name;
            this.url = url;
            this.iconUrl = iconUrl;
        }

        public String getName() {
            return name;
        }

        public String getUrl() {
            return url;
        }

        public String getIconUrl() {
            return iconUrl;
        }
    }

    public static class Field implements EmbedDraft.Field {
        String title;
        String text;
        boolean inline;

        public Field(String title, String text, boolean inline) {
            this.title = title;
            this.text = text;
            this.inline = inline;
        }

        public String getTitle() {
            return title;
        }

        public String getText() {
            return text;
        }

        public boolean isInline() {
            return inline;
        }
    }

    public static class EditableField extends Field implements EmbedDraft.EditableField {
        public EditableField(EmbedDraft.Field field) {
            super(
                    field.getTitle(),
                    field.getText(),
                    field.isInline()
            );
        }

        public EmbedDraft.EditableField setTitle(String title) {
            super.title = title;
            return this;
        }

        public EmbedDraft.EditableField setText(String text) {
            super.text = text;
            return this;
        }

        public EmbedDraft.EditableField setInline(boolean inline) {
            super.inline = inline;
            return this;
        }
    }
}
