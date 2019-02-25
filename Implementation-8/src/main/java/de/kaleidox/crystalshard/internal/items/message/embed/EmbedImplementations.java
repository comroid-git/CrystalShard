package de.kaleidox.crystalshard.internal.items.message.embed;

import java.awt.Color;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import de.kaleidox.crystalshard.api.entity.message.Embed;
import de.kaleidox.util.interfaces.JsonNodeable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import static java.util.Optional.ofNullable;
import static de.kaleidox.util.helpers.JsonHelper.nodeOf;

public final class EmbedImplementations {
    static URL urlOrNull(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException ignored) {
            return null;
        }
    }

    public abstract class Base implements Embed, JsonNodeable {
        protected Embed parent;

        protected String title;
        protected String description;
        protected URL url;
        protected Instant timestamp;
        protected Color color;
        protected Author author;
        protected Thumbnail thumbnail;
        protected List<Field> fields;
        protected Image image;
        protected Video video;
        protected Footer footer;
        protected Provider provider;

        protected Base(
                String title,
                String description,
                URL url,
                Instant timestamp,
                Color color,
                Author author,
                Thumbnail thumbnail,
                List<Field> fields,
                Image image,
                Video video,
                Footer footer,
                Provider provider
        ) {
            this.title = title;
            this.description = description;
            this.url = url;
            this.timestamp = timestamp;
            this.color = color;
            this.author = author;
            this.thumbnail = thumbnail;
            this.fields = new ArrayList<>(fields);
            this.image = image;
            this.video = video;
            this.footer = footer;
            this.provider = provider;
        }

        protected void bindParents(Embed parent) {
            if (author != null) ((EmbedMemberImplementations.Author) author).parent = parent;
            if (thumbnail!= null) ((EmbedMemberImplementations.Thumbnail) thumbnail).parent = parent;
            if (image != null) ((EmbedMemberImplementations.Image) image).parent = parent;
            if (video != null) ((EmbedMemberImplementations.Video) video).parent = parent;
            if (footer != null) ((EmbedMemberImplementations.Footer) footer).parent = parent;
            if (provider != null) ((EmbedMemberImplementations.Provider) provider).parent = parent;
            fields.forEach(field -> ((EmbedMemberImplementations.Field) field).parent = parent);
        }

        @Override
        public Optional<String> getTitle() {
            return ofNullable(title);
        }

        @Override
        public Optional<String> getDescription() {
            return ofNullable(description);
        }

        @Override
        public Optional<URL> getUrl() {
            return ofNullable(url);
        }

        @Override
        public Optional<Instant> getTimestamp() {
            return ofNullable(timestamp);
        }

        @Override
        public Optional<Color> getColor() {
            return ofNullable(color);
        }

        @Override
        public Optional<Author> getAuthor() {
            return ofNullable(author);
        }

        @Override
        public List<Field> getFields() {
            return fields;
        }

        @Override
        public Optional<Thumbnail> getThumbnail() {
            return ofNullable(thumbnail);
        }

        @Override
        public Optional<Image> getImage() {
            return ofNullable(image);
        }

        @Override
        public Optional<Video> getVideo() {
            return ofNullable(video);
        }

        @Override
        public Optional<Footer> getFooter() {
            return ofNullable(footer);
        }

        @Override
        public Optional<Provider> getProvider() {
            return ofNullable(provider);
        }

        @Override
        public JsonNode toJsonNode(ObjectNode root) {
            getTitle().ifPresent(title -> root.set("title", nodeOf(title)));
            getDescription().ifPresent(description -> root.set("description", nodeOf(description)));
            getUrl().map(URL::toExternalForm).ifPresent(url -> root.set("url", nodeOf(url)));
            getTimestamp().map(DateTimeFormatter.ISO_INSTANT::format)
                    .ifPresent(instant -> root.set("timestamp", nodeOf(instant)));
            getColor().map(color -> color.getRGB() & 0xFFFFFF)
                    .ifPresent(rgb -> root.set("rgb", nodeOf(rgb)));
            getAuthor().map(JsonNodeable.class::cast)
                    .orElseThrow(AssertionError::new)
                    .toJsonNode(root.putObject("author"));
            getThumbnail().map(JsonNodeable.class::cast)
                    .orElseThrow(AssertionError::new)
                    .toJsonNode(root.putObject("thumbnail"));
            getFields().stream()
                    .map(JsonNodeable.class::cast)
                    .forEachOrdered(field -> field.toJsonNode(root.withArray("fields").addObject()));
            getImage().map(JsonNodeable.class::cast)
                    .orElseThrow(AssertionError::new)
                    .toJsonNode(root.putObject("image"));
            getFooter().map(JsonNodeable.class::cast)
                    .orElseThrow(AssertionError::new)
                    .toJsonNode(root.putObject("footer"));
            root.set("type", nodeOf("rich"));
            return root;
        }
    }

    public class Unsent extends Base {
        public Unsent(
                String title,
                String description,
                URL url,
                Instant timestamp,
                Color color,
                Author author,
                Thumbnail thumbnail,
                List<Field> fields,
                Image image,
                Video video,
                Footer footer,
                Provider provider) {
            super(
                    title,
                    description,
                    url,
                    timestamp,
                    color,
                    author,
                    thumbnail,
                    fields,
                    image,
                    video,
                    footer,
                    provider
            );
            bindParents(this);
        }

        @Override
        public State getState() {
            return State.UNSENT;
        }

        @Override
        public Updater updater() {
            return null;
        }
    }

    public class Sent extends Base {
        public Sent(JsonNode data) {
            super(
                    data.path("title").asText(null),
                    data.path("description").asText(null),
                    data.has("url") ? urlOrNull(data.get("url").asText()) : null,
                    data.has("timestamp") ? Instant.parse(data.get("timestamp").asText()) : null,
                    data.has("color") ? new Color(data.get("color").asInt()) : null,
                    createAuthor(data.path("author")),
                    createThumbnail(data.path("thumbnail")),
                    createFields(data.path("fields")),
                    createImage(data.path("image")),
                    createVideo(data.path("video")),
                    createFooter(data.path("footer")),
                    createProvider(data.path("provider"))
            );
            bindParents(this);
        }

        @Override
        public State getState() {
            return State.SENT;
        }

        @Override
        public Updater updater() {
            return null;
        }
    }

    private static Embed.Author createAuthor(JsonNode data) {
        if (data.isMissingNode()) return null;
        return new SentEmbedMemberImplementations.Author(data);
    }
    private static Embed.Thumbnail createThumbnail(JsonNode data) {
        if (data.isMissingNode()) return null;
    }
    private static List<Embed.Field> createFields(JsonNode data) {
        if (data.isMissingNode()) return null;
    }
    private static Embed.Image createImage(JsonNode data) {
        if (data.isMissingNode()) return null;
    }
    private static Embed.Video createVideo(JsonNode data) {
        if (data.isMissingNode()) return null;
    }
    private static Embed.Footer createFooter(JsonNode data) {
        if (data.isMissingNode()) return null;
    }
    private static Embed.Provider createProvider(JsonNode data) {
        if (data.isMissingNode()) return null;
    }
}
