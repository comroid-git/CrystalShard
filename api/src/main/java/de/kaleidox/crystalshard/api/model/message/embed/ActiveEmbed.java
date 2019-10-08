package de.kaleidox.crystalshard.api.model.message.embed;

import java.awt.Color;
import java.net.URL;
import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

import de.kaleidox.crystalshard.util.Util;
import de.kaleidox.crystalshard.util.model.serialization.JsonDeserializable;
import de.kaleidox.crystalshard.util.model.serialization.JsonTrait;
import de.kaleidox.crystalshard.util.model.serialization.JsonTraits;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import static de.kaleidox.crystalshard.util.model.serialization.JsonTrait.identity;
import static de.kaleidox.crystalshard.util.model.serialization.JsonTrait.simple;
import static de.kaleidox.crystalshard.util.model.serialization.JsonTrait.underlying;
import static de.kaleidox.crystalshard.util.model.serialization.JsonTrait.underlyingCollective;

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
        JsonTrait<String, String> TITLE = identity(JsonNode::asText, "title");
        JsonTrait<String, String> DESCRIPTION = identity(JsonNode::asText, "description");
        JsonTrait<String, URL> URL = simple(JsonNode::asText, "url", Util::url_rethrow);
        JsonTrait<String, Instant> TIMESTAMP = simple(JsonNode::asText, "timestamp", Instant::parse);
        JsonTrait<Integer, Color> COLOR = simple(JsonNode::asInt, "color", Color::new);
        JsonTrait<JsonNode, Footer> FOOTER = underlying("footer", Footer.class);
        JsonTrait<JsonNode, Image> IMAGE = underlying("image", Image.class);
        JsonTrait<JsonNode, Thumbnail> THUMBNAIL = underlying("thumbnail", Thumbnail.class);
        JsonTrait<JsonNode, Video> VIDEO = underlying("video", Video.class);
        JsonTrait<JsonNode, Provider> PROVIDER = underlying("provider", Provider.class);
        JsonTrait<JsonNode, Author> AUTHOR = underlying("author", Author.class);
        JsonTrait<ArrayNode, Collection<Field>> FIELDS = underlyingCollective("fields", Field.class);
    }

    @JsonTraits(Footer.Trait.class)
    interface Footer extends Embed.Footer, JsonDeserializable {
        @Override
        default String getText() {
        }

        @Override Optional<URL> getIconURL();

        @Override Optional<URL> getProxyIconURL();

        interface Trait {
        }
    }

    @JsonTraits(Footer.Trait.class)
    interface Image extends Embed.Image, JsonDeserializable {
        interface Trait {
        }
    }

    @JsonTraits(Footer.Trait.class)
    interface Thumbnail extends Embed.Thumbnail, JsonDeserializable {
        interface Trait {
        }
    }

    @JsonTraits(Footer.Trait.class)
    interface Video extends Embed.Video, JsonDeserializable {
        interface Trait {
        }
    }

    @JsonTraits(Footer.Trait.class)
    interface Provider extends Embed.Provider, JsonDeserializable {
        interface Trait {
        }
    }

    @JsonTraits(Footer.Trait.class)
    interface Author extends Embed.Author, JsonDeserializable {
        interface Trait {
        }
    }

    @JsonTraits(Footer.Trait.class)
    interface Field extends Embed.Field, JsonDeserializable {
        interface Trait {
        }
    }
}
