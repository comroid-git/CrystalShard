package de.comroid.crystalshard.api.model.message.embed;

import java.awt.Color;
import java.net.URL;
import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

import de.comroid.crystalshard.util.Util;
import de.comroid.crystalshard.util.model.serialization.JsonDeserializable;
import de.comroid.crystalshard.util.model.serialization.JsonBinding;
import de.comroid.crystalshard.util.model.serialization.JsonTraits;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import static de.comroid.crystalshard.util.model.serialization.JsonBinding.identity;
import static de.comroid.crystalshard.util.model.serialization.JsonBinding.simple;
import static de.comroid.crystalshard.util.model.serialization.JsonBinding.underlying;
import static de.comroid.crystalshard.util.model.serialization.JsonBinding.underlyingCollective;

@JsonTraits(ActiveEmbed.Trait.class)
public interface ActiveEmbed extends Embed, JsonDeserializable {
    @Override
    default Optional<String> getTitle() {
        return wrapTraitValue(Trait.TITLE);
    }

    @Override
    default Optional<String> getDescription() {
        return wrapTraitValue(Trait.DESCRIPTION);
    }

    @Override
    default Optional<URL> getURL() {
        return wrapTraitValue(Trait.URL);
    }

    @Override
    default Optional<Instant> getTimestamp() {
        return wrapTraitValue(Trait.TIMESTAMP);
    }

    @Override
    default Optional<Color> getColor() {
        return wrapTraitValue(Trait.COLOR);
    }

    @Override
    default Optional<Footer> getFooter() {
        return wrapTraitValue(Trait.FOOTER);
    }

    @Override
    default Optional<Image> getImage() {
        return wrapTraitValue(Trait.IMAGE);
    }

    @Override
    default Optional<Thumbnail> getThumbnail() {
        return wrapTraitValue(Trait.THUMBNAIL);
    }

    @Override
    default Optional<Video> getVideo() {
        return wrapTraitValue(Trait.VIDEO);
    }

    @Override
    default Optional<Provider> getProvider() {
        return wrapTraitValue(Trait.PROVIDER);
    }

    @Override
    default Optional<Author> getAuthor() {
        return wrapTraitValue(Trait.AUTHOR);
    }

    @Override
    default Collection<Field> getFields() {
        return getTraitValue(Trait.FIELDS);
    }

    interface Trait {
        JsonBinding<String, String> TITLE = identity(JsonNode::asText, "title");
        JsonBinding<String, String> DESCRIPTION = identity(JsonNode::asText, "description");
        JsonBinding<String, URL> URL = simple(JsonNode::asText, "url", Util::createUrl$rethrow);
        JsonBinding<String, Instant> TIMESTAMP = simple(JsonNode::asText, "timestamp", Instant::parse);
        JsonBinding<Integer, Color> COLOR = simple(JsonNode::asInt, "color", Color::new);
        JsonBinding<JsonNode, Footer> FOOTER = underlying("footer", Footer.class);
        JsonBinding<JsonNode, Image> IMAGE = underlying("image", Image.class);
        JsonBinding<JsonNode, Thumbnail> THUMBNAIL = underlying("thumbnail", Thumbnail.class);
        JsonBinding<JsonNode, Video> VIDEO = underlying("video", Video.class);
        JsonBinding<JsonNode, Provider> PROVIDER = underlying("provider", Provider.class);
        JsonBinding<JsonNode, Author> AUTHOR = underlying("author", Author.class);
        JsonBinding<ArrayNode, Collection<Field>> FIELDS = underlyingCollective("fields", Field.class);
    }

    @JsonTraits(Footer.Trait.class)
    interface Footer extends Embed.Footer, JsonDeserializable {
        @Override
        default String getText() {
            return getTraitValue(Trait.TEXT);
        }

        @Override
        default Optional<URL> getIconURL() {
            return wrapTraitValue(Trait.ICON_URL);
        }

        @Override
        default Optional<URL> getProxyIconURL() {
            return wrapTraitValue(Trait.PROXY_ICON_URL);
        }

        interface Trait {
            JsonBinding<String, String> TEXT = identity(JsonNode::asText, "text");
            JsonBinding<String, URL> ICON_URL = simple(JsonNode::asText, "icon_url", Util::createUrl$rethrow);
            JsonBinding<String, URL> PROXY_ICON_URL = simple(JsonNode::asText, "proxy_icon_url", Util::createUrl$rethrow);
        }
    }

    @JsonTraits(Footer.Trait.class)
    interface Image extends Embed.Image, JsonDeserializable {
        @Override
        default Optional<URL> getURL() {
            return wrapTraitValue(Trait.URL);
        }

        @Override
        default Optional<URL> getProxyURL() {
            return wrapTraitValue(Trait.PROXY_URL);
        }

        @Override
        default Optional<Integer> getHeight() {
            return wrapTraitValue(Trait.HEIGHT);
        }

        @Override
        default Optional<Integer> getWidth() {
            return wrapTraitValue(Trait.WIDTH);
        }

        interface Trait {
            JsonBinding<String, URL> URL = simple(JsonNode::asText, "url", Util::createUrl$rethrow);
            JsonBinding<String, URL> PROXY_URL = simple(JsonNode::asText, "proxy_url", Util::createUrl$rethrow);
            JsonBinding<Integer, Integer> HEIGHT = identity(JsonNode::asInt, "height");
            JsonBinding<Integer, Integer> WIDTH = identity(JsonNode::asInt, "width");
        }
    }

    @JsonTraits(Footer.Trait.class)
    interface Thumbnail extends Embed.Thumbnail, JsonDeserializable {
        @Override
        default Optional<URL> getURL() {
            return wrapTraitValue(Trait.URL);
        }

        @Override
        default Optional<URL> getProxyURL() {
            return wrapTraitValue(Trait.PROXY_URL);
        }

        @Override
        default Optional<Integer> getHeight() {
            return wrapTraitValue(Trait.HEIGHT);
        }

        @Override
        default Optional<Integer> getWidth() {
            return wrapTraitValue(Trait.WIDTH);
        }

        interface Trait {
            JsonBinding<String, URL> URL = simple(JsonNode::asText, "url", Util::createUrl$rethrow);
            JsonBinding<String, URL> PROXY_URL = simple(JsonNode::asText, "proxy_url", Util::createUrl$rethrow);
            JsonBinding<Integer, Integer> HEIGHT = identity(JsonNode::asInt, "height");
            JsonBinding<Integer, Integer> WIDTH = identity(JsonNode::asInt, "width");
        }
    }

    @JsonTraits(Footer.Trait.class)
    interface Video extends Embed.Video, JsonDeserializable {
        @Override
        default Optional<URL> getURL() {
            return wrapTraitValue(Trait.URL);
        }

        @Override
        default Optional<Integer> getHeight() {
            return wrapTraitValue(Trait.HEIGHT);
        }

        @Override
        default Optional<Integer> getWidth() {
            return wrapTraitValue(Trait.WIDTH);
        }

        interface Trait {
            JsonBinding<String, URL> URL = simple(JsonNode::asText, "url", Util::createUrl$rethrow);
            JsonBinding<Integer, Integer> HEIGHT = identity(JsonNode::asInt, "height");
            JsonBinding<Integer, Integer> WIDTH = identity(JsonNode::asInt, "width");
        }
    }

    @JsonTraits(Footer.Trait.class)
    interface Provider extends Embed.Provider, JsonDeserializable {
        @Override
        default Optional<String> getName() {
            return wrapTraitValue(Trait.NAME);
        }

        @Override
        default Optional<URL> getURL() {
            return wrapTraitValue(Trait.URL);
        }

        interface Trait {
            JsonBinding<String, String> NAME = identity(JsonNode::asText, "name");
            JsonBinding<String, URL> URL = simple(JsonNode::asText, "url", Util::createUrl$rethrow);
        }
    }

    @JsonTraits(Footer.Trait.class)
    interface Author extends Embed.Author, JsonDeserializable {
        @Override
        default Optional<String> getName() {
            return wrapTraitValue(Trait.NAME);
        }

        @Override
        default Optional<URL> getURL() {
            return wrapTraitValue(Trait.URL);
        }

        @Override
        default Optional<URL> getIconURL() {
            return wrapTraitValue(Trait.ICON_URL);
        }

        @Override
        default Optional<URL> getProxyIconURL() {
            return wrapTraitValue(Trait.PROXY_ICON_URL);
        }

        interface Trait {
            JsonBinding<String, String> NAME = identity(JsonNode::asText, "name");
            JsonBinding<String,URL> URL = simple(JsonNode::asText, "url", Util::createUrl$rethrow);
            JsonBinding<String, URL> ICON_URL = simple(JsonNode::asText, "icon_url", Util::createUrl$rethrow);
            JsonBinding<String,URL> PROXY_ICON_URL = simple(JsonNode::asText, "proxy_icon_url", Util::createUrl$rethrow);
        }
    }

    @JsonTraits(Footer.Trait.class)
    interface Field extends Embed.Field, JsonDeserializable {
        @Override 
        default String getName() {
            return getTraitValue(Trait.NAME);
        }

        @Override
        default String getValue() {
            return getTraitValue(Trait.VALUE);
        }

        @Override
        default boolean isInline() {
            return getTraitValue(Trait.INLINE);
        }

        interface Trait {
            JsonBinding<String, String> NAME = identity(JsonNode::asText, "name");
            JsonBinding<String, String> VALUE = identity(JsonNode::asText, "value");
            JsonBinding<Boolean, Boolean> INLINE = identity(JsonNode::asBoolean, "inline");
        }
    }
}
