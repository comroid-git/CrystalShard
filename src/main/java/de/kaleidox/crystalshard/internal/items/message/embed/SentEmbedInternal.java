package de.kaleidox.crystalshard.internal.items.message.embed;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.main.items.message.embed.Embed;
import de.kaleidox.crystalshard.main.items.message.embed.EmbedDraft;
import de.kaleidox.crystalshard.main.items.message.embed.SentEmbed;
import de.kaleidox.util.UrlHelper;

import java.awt.*;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Optional;

@SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "FieldCanBeLocal"})
public class SentEmbedInternal implements SentEmbed {
    private Optional<String> title;
    private Optional<String> description;
    private Optional<URL> url;
    private Optional<Instant> timestamp;
    private Optional<Color> color;
    private Optional<SentEmbed.Footer> footer;
    private Optional<SentEmbed.Image> image;
    private Optional<SentEmbed.Thumbnail> thumbnail;
    private Optional<SentEmbed.Author> author;
    private Optional<SentEmbed.Video> video;
    private Optional<SentEmbed.Provider> provider;
    private ArrayList<SentEmbed.Field> fields;

    public SentEmbedInternal(JsonNode data) {
        this.title = Optional.ofNullable(data.get("title").asText(null));
        this.description = Optional.ofNullable(data.get("description").asText(null));
        this.url = Optional.ofNullable(UrlHelper.orNull(data.get("url").asText(null)));
        this.timestamp = Optional.ofNullable(data.has("timestamp") ?
                Instant.parse(data.get("timestamp").asText()) : null);
        this.color = Optional.ofNullable(data.has("color") ?
                new Color(data.get("color").asInt()) : null);
        this.footer = Optional.ofNullable(data.has("footer") ?
                new Footer(data.get("footer")) : null);
        this.image = Optional.ofNullable(data.has("image") ?
                new Image(data.get("image")) : null);
        this.thumbnail = Optional.ofNullable(data.has("thumbnail") ?
                new Thumbnail(data.get("thumbnail")) : null);
        this.author = Optional.ofNullable(data.has("author") ?
                new Author(data.get("author")) : null);
        this.provider = Optional.ofNullable(data.has("provider") ?
                new Provider(data.get("provider")) : null);
        this.video = Optional.ofNullable(data.has("video") ?
                new Video(data.get("video")) : null);
        for (JsonNode node : data.get("fields")) {
            fields.add(new Field(node));
        }
    }

    @Override
    public Optional<EmbedDraft> toEmbedDraft() {
        return Optional.empty();
    }

    @Override
    public Optional<Builder> toBuilder() {
        Builder builder = Embed.BUILDER()
                .setTitle(title.orElse(null))
                .setDescription(description.orElse(null))
                .setUrl(url.map(URL::toExternalForm).orElse(null))
                .setTimestamp(timestamp.orElse(null))
                .setColor(color.orElse(null))
                .setFooter(footer.map(SentEmbed.Footer::toDraft).orElse(null))
                .setImage(image.map(SentEmbed.Image::toDraft).orElse(null))
                .setThumbnail(thumbnail.map(SentEmbed.Thumbnail::toDraft).orElse(null))
                .setAuthor(author.map(SentEmbed.Author::toDraft).orElse(null));
        fields.forEach(field -> builder.addField(field.toDraft()));
        return Optional.of(builder);
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
