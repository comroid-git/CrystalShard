package de.kaleidox.crystalshard.internal.items.message.embed;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.kaleidox.crystalshard.internal.util.Container;
import de.kaleidox.crystalshard.main.items.message.embed.Embed;
import de.kaleidox.crystalshard.main.items.message.embed.EmbedDraft;
import de.kaleidox.crystalshard.main.items.message.embed.SentEmbed;
import de.kaleidox.logging.Logger;
import de.kaleidox.util.JsonHelper;
import de.kaleidox.util.UrlHelper;

import java.awt.*;
import java.net.URL;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SuppressWarnings({"unused", "WeakerAccess"})
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
        Builder builder = Embed.BUILDER()
                .setTitle(title)
                .setDescription(description)
                .setUrl(url == null ? null : url.toExternalForm())
                .setTimestamp(timestamp)
                .setColor(color)
                .setFooter(footer)
                .setImage(image)
                .setThumbnail(thumbnail)
                .setAuthor(author);
        fields.forEach(builder::addField);
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

    public ObjectNode toJsonNode(ObjectNode object) {
        String footerText = null;
        String footerIconUrl = null;
        String imageUrl = null;
        String authorName = null;
        String authorUrl = null;
        String authorIconUrl = null;
        String thumbnailUrl = null;
        Container footerIconContainer = null;
        Container imageContainer = null;
        Container authorIconContainer = null;
        Container thumbnailContainer = null;
        if (footer != null) {
            footerText = footer.getText();
            footerIconUrl = footer.getIconUrl().map(URL::toExternalForm).orElse(null);
            footerIconContainer = footer.getContainer();
        }
        if (image != null) {
            imageUrl = image.getUrl().map(URL::toExternalForm).orElse(null);
            imageContainer = image.getContainer();
        }
        if (author != null) {
            authorName = author.getName();
            authorUrl = author.getUrl().map(URL::toExternalForm).orElse(null);
            authorIconUrl = author.getIconUrl().map(URL::toExternalForm).orElse(null);
            authorIconContainer = author.getContainer();
        }
        if (thumbnail != null) {
            thumbnailUrl = thumbnail.getUrl().map(URL::toExternalForm).orElse(null);
            thumbnailContainer = thumbnail.getContainer();
        }

        object.put("type", "rich");
        if (title != null && !title.equals("")) {
            object.put("title", title);
        }
        if (description != null && !description.equals("")) {
            object.put("description", description);
        }
        if (url != null && !url.equals("")) {
            object.put("url", JsonHelper.nodeOf(url));
        }
        if (color != null) {
            object.put("color", color.getRGB() & 0xFFFFFF);
        }
        if (timestamp != null) {
            object.put("timestamp", DateTimeFormatter.ISO_INSTANT.format(timestamp));
        }
        if ((footerText != null && !footerText.equals("")) || (footerIconUrl != null && !footerIconUrl.equals(""))) {
            ObjectNode footer = object.putObject("footer");
            if (footerText != null && !footerText.equals("")) {
                footer.put("text", footerText);
            }
            if (footerIconUrl != null && !footerIconUrl.equals("")) {
                footer.put("icon_url", footerIconUrl);
            }
            if (footerIconContainer != null) {
                footer.put("icon_url", "attachment://" + footerIconContainer.getFullName());
            }
        }
        if (imageUrl != null && !imageUrl.equals("")) {
            object.putObject("image").put("url", imageUrl);
        }
        if (imageContainer != null) {
            object.putObject("image").put("url", "attachment://" + imageContainer.getFullName());
        }
        if (authorName != null && !authorName.equals("")) {
            ObjectNode author = object.putObject("author");
            author.put("name", authorName);
            if (authorUrl != null && !authorUrl.equals("")) {
                author.put("url", authorUrl);
            }
            if (authorIconUrl != null && !authorIconUrl.equals("")) {
                author.put("icon_url", authorIconUrl);
            }
            if (authorIconContainer != null) {
                author.put("url", "attachment://" + authorIconContainer.getFullName());
            }
        }
        if (thumbnailUrl != null && !thumbnailUrl.equals("")) {
            object.putObject("thumbnail").put("url", thumbnailUrl);
        }
        if (thumbnailContainer != null) {
            object.putObject("thumbnail").put("url", "attachment://" + thumbnailContainer.getFullName());
        }
        if (fields.size() > 0) {
            ArrayNode jsonFields = object.putArray("fields");
            for (EmbedDraft.Field field : fields) {
                ObjectNode jsonField = jsonFields.addObject();
                jsonField.put("name", field.getTitle());
                jsonField.put("value", field.getText());
                jsonField.put("inline", field.isInline());
            }
        }
        return object;
    }

    public static class Footer implements EmbedDraft.Footer {
        private final String name;
        private final URL url;

        public Footer(String name, String iconUrl) {
            this.name = name;
            this.url = UrlHelper.orNull(iconUrl);
        }

        public String getText() {
            return name;
        }

        public Optional<URL> getIconUrl() {
            return Optional.ofNullable(url);
        }

        @Override
        public Container getContainer() {
            return null;
        }
    }

    public static class Image implements EmbedDraft.Image {
        private final URL url;

        public Image(String url) {
            this.url = UrlHelper.require(url);
        }

        public Optional<URL> getUrl() {
            return Optional.ofNullable(url);
        }

        @Override
        public Container getContainer() {
            return null;
        }
    }

    public static class Thumbnail implements EmbedDraft.Thumbnail {
        private final URL url;

        public Thumbnail(String url) {
            this.url = UrlHelper.require(url);
        }

        public Optional<URL> getUrl() {
            return Optional.ofNullable(url);
        }

        @Override
        public Container getContainer() {
            return null;
        }
    }

    public static class Author implements EmbedDraft.Author {
        private final String name;
        private final URL url;
        private final URL iconUrl;

        public Author(String name, String url, String iconUrl) {
            this.name = name;
            this.url = UrlHelper.orNull(url);
            this.iconUrl = UrlHelper.orNull(iconUrl);
        }

        public String getName() {
            return name;
        }

        public Optional<URL> getUrl() {
            return Optional.ofNullable(url);
        }

        public Optional<URL> getIconUrl() {
            return Optional.ofNullable(iconUrl);
        }

        @Override
        public Container getContainer() {
            return null;
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
