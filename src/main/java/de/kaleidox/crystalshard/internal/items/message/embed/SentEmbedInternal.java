package de.kaleidox.crystalshard.internal.items.message.embed;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.main.items.message.embed.Embed;
import de.kaleidox.crystalshard.main.items.message.embed.EmbedDraft;
import de.kaleidox.crystalshard.main.items.message.embed.SentEmbed;
import de.kaleidox.util.helpers.UrlHelper;

import java.awt.*;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

@SuppressWarnings("FieldCanBeLocal")
public class SentEmbedInternal implements SentEmbed {
    private final String title;
    private final String description;
    private final URL url;
    private final Instant timestamp;
    private final Color color;
    private final SentEmbed.Footer footer;
    private final SentEmbed.Image image;
    private final SentEmbed.Thumbnail thumbnail;
    private final SentEmbed.Author author;
    private final SentEmbed.Video video;
    private final SentEmbed.Provider provider;
    private final ArrayList<SentEmbed.Field> fields;

    public SentEmbedInternal(JsonNode data) {
        this.title = data.get("title").asText(null);
        this.description = data.get("description").asText(null);
        this.url = UrlHelper.orNull(data.get("url").asText(null));
        this.timestamp = (data.has("timestamp") ?
                Instant.parse(data.get("timestamp").asText()) : null);
        this.color = (data.has("color") ?
                new Color(data.get("color").asInt()) : null);
        this.footer = (data.has("footer") ?
                new Footer(data.get("footer")) : null);
        this.image = (data.has("image") ?
                new Image(data.get("image")) : null);
        this.thumbnail = (data.has("thumbnail") ?
                new Thumbnail(data.get("thumbnail")) : null);
        this.author = (data.has("author") ?
                new Author(data.get("author")) : null);
        this.provider = (data.has("provider") ?
                new Provider(data.get("provider")) : null);
        this.video = (data.has("video") ?
                new Video(data.get("video")) : null);
        this.fields = new ArrayList<>();
        for (JsonNode node : data.get("fields")) {
            fields.add(new Field(node));
        }
    }

    @Override
    public Optional<EmbedDraft> toEmbedDraft() {
        return toBuilder().map(Builder::build);
    }

    @Override
    public Optional<Builder> toBuilder() {
        Builder builder = Embed.BUILDER()
                .setTitle(title)
                .setDescription(description)
                .setUrl(Objects.nonNull(url) ? url.toExternalForm() : null)
                .setTimestamp(timestamp)
                .setColor(color)
                .setFooter(Objects.nonNull(footer) ? footer.toDraft() : null)
                .setImage(Objects.nonNull(image) ? image.toDraft() : null)
                .setThumbnail(Objects.nonNull(thumbnail) ? thumbnail.toDraft() : null)
                .setAuthor(Objects.nonNull(author) ? author.toDraft() : null);
        fields.forEach(field -> builder.addField(field.toDraft()));
        return Optional.of(builder);
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
    public Optional<SentEmbed.Footer> getFooter() {
        return Optional.ofNullable(footer);
    }

    @Override
    public Optional<SentEmbed.Image> getImage() {
        return Optional.ofNullable(image);
    }

    @Override
    public Optional<SentEmbed.Author> getAuthor() {
        return Optional.ofNullable(author);
    }

    @Override
    public Optional<SentEmbed.Thumbnail> getThumbail() {
        return Optional.ofNullable(thumbnail);
    }

    @Override
    public Optional<SentEmbed.Video> getVideo() {
        return Optional.ofNullable(video);
    }

    @Override
    public Optional<SentEmbed.Provider> getProvider() {
        return Optional.ofNullable(provider);
    }

    @Override
    public Collection<SentEmbed.Field> getFields() {
        return Collections.unmodifiableList(fields);
    }

    public class Footer implements SentEmbed.Footer {
        public Footer(JsonNode data) {
        }

        @Override
        public String getText() {
            return null;
        }

        @Override
        public Optional<URL> getIconUrl() {
            return Optional.empty();
        }
    }

    public class Image implements SentEmbed.Image {
        public Image(JsonNode data) {
        }

        @Override
        public Optional<URL> getUrl() {
            return Optional.empty();
        }
    }

    public class Author implements SentEmbed.Author {
        public Author(JsonNode data) {
        }

        @Override
        public Optional<URL> getUrl() {
            return Optional.empty();
        }

        @Override
        public Optional<URL> getIconUrl() {
            return Optional.empty();
        }

        @Override
        public String getName() {
            return null;
        }
    }

    public class Thumbnail implements SentEmbed.Thumbnail {
        public Thumbnail(JsonNode data) {
        }

        @Override
        public Optional<URL> getUrl() {
            return Optional.empty();
        }
    }

    public class Field implements SentEmbed.Field {
        private final String title;
        private final String text;
        private final boolean inline;

        public Field(JsonNode data) {
            this.title = data.get("title").asText();
            this.text = data.get("text").asText();
            this.inline = data.get("inline").asBoolean();
        }

        @Override
        public String getTitle() {
            return title;
        }

        @Override
        public String getText() {
            return text;
        }

        @Override
        public boolean isInline() {
            return inline;
        }
    }

    public class Video implements SentEmbed.Video {
        public Video(JsonNode data) {
        }

        @Override
        public Optional<URL> getUrl() {
            return Optional.empty();
        }
    }

    public class Provider implements SentEmbed.Provider {
        public Provider(JsonNode data) {
        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public Optional<URL> getUrl() {
            return Optional.empty();
        }
    }
}
