package de.kaleidox.crystalshard.internal.items.message.embed;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.items.message.embed.Embed;
import de.kaleidox.crystalshard.main.items.message.embed.EmbedDraft;
import de.kaleidox.crystalshard.main.items.message.embed.SentEmbed;
import de.kaleidox.crystalshard.util.helpers.UrlHelper;

import java.awt.*;
import java.net.URL;
import java.time.Instant;
import java.util.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
    private final Message message;

    public SentEmbedInternal(Message message, JsonNode data) {
        this.message = message;
        this.title = data.path("title")
                .asText(null);
        this.description = data.path("description")
                .asText(null);
        this.url = UrlHelper.orNull(data.path("url")
                .asText(null));
        this.timestamp = (data.has("timestamp") ? Instant.parse(data.path("timestamp")
                .asText()) : null);
        this.color = (data.has("color") ? new Color(data.path("color")
                .asInt()) : null);
        this.footer = (data.has("footer") ? new Footer(data.path("footer")) : null);
        this.image = (data.has("image") ? new Image(data.path("image")) : null);
        this.thumbnail = (data.has("thumbnail") ? new Thumbnail(data.path("thumbnail")) : null);
        this.author = (data.has("author") ? new Author(data.path("author")) : null);
        this.provider = (data.has("provider") ? new Provider(data.path("provider")) : null);
        this.video = (data.has("video") ? new Video(data.path("video")) : null);
        this.fields = new ArrayList<>();
        for (JsonNode node : data.path("fields")) {
            fields.add(new Field(node));
        }
    }

    // Override Methods
    @Override
    public EmbedDraft toEmbedDraft() {
        return toBuilder().build();
    }

    @Override
    public Builder toBuilder() {
        Builder builder = Embed.BUILDER()
                .setTitle(title)
                .setDescription(description)
                .setUrl(Objects.nonNull(url) ? url.toExternalForm() : null)
                .setTimestamp(
                        timestamp)
                .setColor(color)
                .setFooter(Objects.nonNull(footer) ? footer.toDraft() : null)
                .setImage(
                        Objects.nonNull(image) ? image.toDraft() : null)
                .setThumbnail(Objects.nonNull(thumbnail) ? thumbnail.toDraft() : null)
                .setAuthor(
                        Objects.nonNull(author) ? author.toDraft() : null);
        fields.forEach(field -> builder.addField(field.toDraft()));
        return builder;
    }

    @Override
    public Message getMessage() {
        return message;
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

    @Override
    public Updater getUpdater() throws IllegalAccessException {
        if (!message.getAuthor()
                .isYourself()) throw new IllegalAccessException("You cannot edit someone elses embed!");
        return new Updater(this);
    }

    @Override
    public CompletableFuture<SentEmbed> setTitle(String title) {
        Builder builder = toBuilder();
        builder.setTitle(title);
        return message.edit(builder.build())
                .thenApply(msg -> msg.getEmbed()
                        .orElseThrow(AssertionError::new));
    }

    @Override
    public CompletableFuture<SentEmbed> setDescription(String description) {
        Builder builder = toBuilder();
        builder.setDescription(description);
        return message.edit(builder.build())
                .thenApply(msg -> msg.getEmbed()
                        .orElseThrow(AssertionError::new));
    }

    @Override
    public CompletableFuture<SentEmbed> setUrl(String url) {
        Builder builder = toBuilder();
        builder.setUrl(url);
        return message.edit(builder.build())
                .thenApply(msg -> msg.getEmbed()
                        .orElseThrow(AssertionError::new));
    }

    @Override
    public CompletableFuture<SentEmbed> setTimestamp(Instant timestamp) {
        Builder builder = toBuilder();
        builder.setTimestamp(timestamp);
        return message.edit(builder.build())
                .thenApply(msg -> msg.getEmbed()
                        .orElseThrow(AssertionError::new));
    }

    @Override
    public CompletableFuture<SentEmbed> setColor(Color color) {
        Builder builder = toBuilder();
        builder.setColor(color);
        return message.edit(builder.build())
                .thenApply(msg -> msg.getEmbed()
                        .orElseThrow(AssertionError::new));
    }

    @Override
    public CompletableFuture<SentEmbed> modifyFields(Consumer<Collection<EmbedDraft.EditableField>> fieldModifier) {
        List<EmbedDraft.EditableField> fields = getFields().stream()
                .map(SentEmbed.Field::toDraft)
                .map(EmbedDraftInternal.EditableField::new)
                .collect(Collectors
                        .toList());
        fieldModifier.accept(fields);
        Builder builder = toBuilder();
        builder.removeAllFields();
        fields.forEach(builder::addField);
        return message.edit(builder.build())
                .thenApply(msg -> msg.getEmbed()
                        .orElseThrow(AssertionError::new));
    }

    @Override
    public CompletableFuture<SentEmbed> modifyFields(Predicate<SentEmbed.Field> tester, Consumer<EmbedDraft.EditableField> fieldModifier) {
        Builder builder = toBuilder();
        builder.removeAllFields();
        getFields().stream()
                .map(field -> {
                    EmbedDraft.Field draftField = field.toDraft();
                    if (tester.test(field)) {
                        EmbedDraftInternal.EditableField editableField = new EmbedDraftInternal.EditableField(draftField);
                        fieldModifier.accept(editableField);
                        return editableField;
                    }
                    return draftField;
                })
                .forEach(builder::addField);
        return message.edit(builder.build())
                .thenApply(msg -> msg.getEmbed()
                        .orElseThrow(AssertionError::new));
    }

    public class Updater implements SentEmbed.Updater {
        private final Builder builder;
        private final Message message;

        public Updater(SentEmbed embed) {
            this.builder = embed.toBuilder();
            this.message = embed.getMessage();
        }

        // Override Methods
        @Override
        public SentEmbed.Updater setTitle(String title) {
            builder.setTitle(title);
            return this;
        }

        @Override
        public SentEmbed.Updater setDescription(String description) {
            builder.setDescription(description);
            return this;
        }

        @Override
        public SentEmbed.Updater setUrl(String url) {
            builder.setUrl(url);
            return this;
        }

        @Override
        public SentEmbed.Updater setTimestamp(Instant timestamp) {
            builder.setTimestamp(timestamp);
            return this;
        }

        @Override
        public SentEmbed.Updater setColor(Color color) {
            builder.setColor(color);
            return this;
        }

        @Override
        public SentEmbed.Updater modifyFields(Consumer<Collection<EmbedDraft.EditableField>> fieldModifier) {
            Collection<EmbedDraft.EditableField> fields = builder.getFields()
                    .stream()
                    .map(EmbedDraftInternal.EditableField::new)
                    .collect(Collectors.toList());
            fieldModifier.accept(fields);
            builder.removeAllFields();
            fields.forEach(builder::addField);
            return this;
        }

        @Override
        public SentEmbed.Updater modifyFields(Predicate<EmbedDraft.Field> tester, Consumer<EmbedDraft.EditableField> fieldModifier) {
            Collection<EmbedDraft.EditableField> fields = builder.getFields()
                    .stream()
                    .map(EmbedDraftInternal.EditableField::new)
                    .collect(Collectors.toList());
            builder.removeAllFields();
            fields.stream()
                    .map(field -> {
                        if (tester.test(field)) {
                            EmbedDraftInternal.EditableField editableField = new EmbedDraftInternal.EditableField(field);
                            fieldModifier.accept(editableField);
                            return editableField;
                        }
                        return field;
                    })
                    .forEach(builder::addField);
            return this;
        }

        @Override
        public CompletableFuture<SentEmbed> update() {
            return message.edit(builder.build())
                    .thenApply(msg -> msg.getEmbed()
                            .orElseThrow(AssertionError::new));
        }
    }

    public class Footer implements SentEmbed.Footer {
        public Footer(JsonNode data) {
        }

        // Override Methods
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

        // Override Methods
        @Override
        public Optional<URL> getUrl() {
            return Optional.empty();
        }
    }

    public class Author implements SentEmbed.Author {
        public Author(JsonNode data) {
        }

        // Override Methods
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

        // Override Methods
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
            this.title = data.path("title")
                    .asText();
            this.text = data.path("text")
                    .asText();
            this.inline = data.path("inline")
                    .asBoolean(false);
        }

        // Override Methods
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

        // Override Methods
        @Override
        public Optional<URL> getUrl() {
            return Optional.empty();
        }
    }

    public class Provider implements SentEmbed.Provider {
        public Provider(JsonNode data) {
        }

        // Override Methods
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
