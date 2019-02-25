package de.kaleidox.crystalshard.internal.items.message.embed;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import de.kaleidox.crystalshard.api.entity.message.Embed;
import de.kaleidox.crystalshard.api.entity.message.Message;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static de.kaleidox.crystalshard.internal.items.message.embed.EmbedImplementations.urlOrNull;

public final class EmbedMemberImplementations {
    // Partial Members
    public static abstract class Member implements Embed.Member {
        protected Embed parent;

        @Override
        public Embed getEmbed() {
            return parent;
        }

        @Override
        public Embed.State getState() {
            return parent.getState();
        }
    }

    public static abstract class AttachmentMember extends Member implements Embed.AttachmentMember {
        protected UUID uuid;
        protected InputStream inputStream;

        URI fileUri;

        public void attach(InputStream stream, String fileType) {
            try {
                uuid = UUID.randomUUID();
                fileUri = new URI("attachment://" + uuid.toString() + "." + fileType);
                inputStream = stream;
            } catch (Exception e) {
                throw new AssertionError("Unexpected exception thrown:", e);
            }
        }

        @Override
        public boolean hasAttachment() {
            return uuid != null && inputStream != null;
        }

        public void detachFor(URL url) throws AssertionError {
            try {
                uuid = null;
                inputStream = null;

                fileUri = url.toURI();
            } catch (URISyntaxException e) {
                throw new AssertionError("Unexpected exception occured", e);
            }
        }
    }

    // Full Members
    public static class Author extends AttachmentMember implements Embed.Author {
        String name;
        URL url;
        URL proxyIcon;

        private Author(@NotNull String name, URL url, URL icon, URL proxyIcon) {
            super();
            this.name = requireNonNull(name, "Name must not be null");
            this.url = url;
            if (icon != null) detachFor(icon);
            this.proxyIcon = proxyIcon;
        }

        public Author(String name, URL url, URL icon) {
            this(name, url, icon, null);
        }

        public Author(JsonNode data) {
            this(
                    data.get("name").asText(),
                    data.has("url") ? urlOrNull(data.get("url").asText()) : null,
                    data.has("icon_url") ? urlOrNull(data.get("icon_url").asText()) : null,
                    data.has("icon_proxy_url") ? urlOrNull(data.get("icon_proxy_url").asText()) : null
            );
        }

        @Override
        public String getName() {
            assert name != null;
            return name;
        }

        @Override
        public Optional<URL> getUrl() {
            return ofNullable(url);
        }

        @Override
        public Optional<URI> getIconUri() {
            return ofNullable(fileUri);
        }

        @Override
        public Optional<URL> getProxyIconUrl() {
            return ofNullable(proxyIcon);
        }

        @Override
        public Updater updater() {
            return new Updater() {
                @Override
                public Updater setName(String name) throws IllegalArgumentException {
                    Author.this.name = name;
                    return this;
                }

                @Override
                public Updater setUrl(URL url) {
                    Author.this.url = url;
                    return this;
                }

                @Override
                public Updater setIconUrl(URL url) throws AssertionError {
                    Author.this.detachFor(url);
                    return this;
                }

                @Override
                public CompletableFuture<Message> update() throws IllegalStateException {
                    Message message = getEmbed().getMessage();
                    if (getState() == Embed.State.UNSENT)
                        return CompletableFuture.completedFuture(message);
                    ((EmbedImplementations.Sent) getEmbed()).author = Author.this;
                    return message.edit(getEmbed());
                }

                @Override
                public Embed.Author build() throws IllegalStateException {
                    return Author.this;
                }
            };
        }
    }

    public static class Thumbnail extends AttachmentMember implements Embed.Thumbnail {
        URL proxyImage;

        private Thumbnail(@NotNull URL image, URL proxyImage) {
            detachFor(requireNonNull(image, "Image must not be null"));
            this.proxyImage = proxyImage;
        }

        public Thumbnail(URL image) {
            this(image, null);
        }

        public Thumbnail(JsonNode data) {
            this(
                    requireNonNull(urlOrNull(data.get("url").asText()), "Invalid URL"),
                    urlOrNull(data.get("proxy_url").asText())
            );
        }

        @Override
        public URI getImageUri() {
            return fileUri;
        }

        @Override
        public Optional<URL> getProxyImageUrl() {
            return ofNullable(proxyImage);
        }

        @Override
        public Updater updater() {
            return new Updater() {
                @Override
                public Updater setImageUrl(URL url) {
                    Thumbnail.this.detachFor(url);
                    return this;
                }

                @Override
                public CompletableFuture<Message> update() throws IllegalStateException {
                    Message message = getEmbed().getMessage();
                    if (getState() == Embed.State.UNSENT)
                        return CompletableFuture.completedFuture(message);
                    ((EmbedImplementations.Sent) getEmbed()).thumbnail = Thumbnail.this;
                    return message.edit(getEmbed());
                }

                @Override
                public Embed.Thumbnail build() throws IllegalStateException {
                    return Thumbnail.this;
                }
            };
        }
    }

    public static class Field extends Member implements Embed.Field {
        String title;
        String content;
        boolean inline;

        public Field(@NotNull String title, @NotNull String content, boolean inline) {
            this.title = title;
            this.content = content;
            this.inline = inline;
        }

        public Field(JsonNode data) {
            this(
                    data.get("name").asText(),
                    data.get("value").asText(),
                    data.path("inline").asBoolean(false)
            );
        }

        @Override
        public String getTitle() {
            return title;
        }

        @Override
        public String getContent() {
            return content;
        }

        @Override
        public boolean isInline() {
            return inline;
        }

        @Override
        public Updater updater() {
            return new Updater() {
                @Override
                public Updater setTitle(String title) throws IllegalArgumentException {
                    Field.this.title = title;
                    return this;
                }

                @Override
                public Updater setContent(String content) throws IllegalArgumentException {
                    Field.this.content = content;
                    return this;
                }

                @Override
                public Updater setInline(boolean inline) {
                    Field.this.inline = inline;
                    return this;
                }

                @Override
                public CompletableFuture<Message> update() throws IllegalStateException {
                    Message message = getEmbed().getMessage();
                    if (getState() == Embed.State.UNSENT)
                        return CompletableFuture.completedFuture(message);
                    List<Embed.Field> fields = ((EmbedImplementations.Sent) getEmbed()).fields;
                    int c = 0;
                    while (true)
                        if (fields.get(c++).hashCode() == this.hashCode() || c > fields.size()) {
                            if (c <= fields.size()) c++;
                            break;
                        }
                    ((EmbedImplementations.Sent) getEmbed()).fields.set(c, Field.this);
                    return message.edit(getEmbed());
                }

                @Override
                public Embed.Field build() throws IllegalStateException {
                    return Field.this;
                }
            };
        }
    }

    public static class Image extends AttachmentMember implements Embed.Image {

        @Override
        public URI getImageUri() {
            return fileUri;
        }

        @Override
        public Optional<URL> getProxyImageUrl() {
            return empty();
        }

        @Override
        public Updater updater() {
            return null; // todo
        }
    }

    public static class Video extends Member implements Embed.Video {
        @Override
        public URL getVideoUrl() {
            return null; // todo
        }
    }

    public static class Footer extends AttachmentMember implements Embed.Footer {
        String text;
        URL url;

        @Override
        public String getText() {
            return text;
        }

        @Override
        public Optional<URL> getUrl() {
            return ofNullable(url);
        }

        @Override
        public Optional<URI> getIconUri() {
            return ofNullable(fileUri);
        }

        @Override
        public Optional<URL> getProxyIconUrl() {
            return empty();
        }

        @Override
        public Updater updater() {
            return null; // todo
        }
    }

    public static class Provider extends Member implements Embed.Provider { // todo
        @Override
        public String getName() {
            return null;
        }

        @Override
        public URL getUrl() {
            return null;
        }
    }
}
