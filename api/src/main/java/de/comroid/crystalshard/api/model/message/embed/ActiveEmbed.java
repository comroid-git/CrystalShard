package de.comroid.crystalshard.api.model.message.embed;

import java.awt.Color;
import java.net.URL;
import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

import de.comroid.crystalshard.util.Util;
import de.comroid.crystalshard.util.model.serialization.JsonBinding;
import de.comroid.crystalshard.util.model.serialization.JsonDeserializable;
import de.comroid.crystalshard.util.model.serialization.JsonTraits;

import com.alibaba.fastjson.JSONObject;

import static de.comroid.crystalshard.util.model.serialization.JsonBinding.identity;
import static de.comroid.crystalshard.util.model.serialization.JsonBinding.serializableCollection;
import static de.comroid.crystalshard.util.model.serialization.JsonBinding.serialize;
import static de.comroid.crystalshard.util.model.serialization.JsonBinding.simple;

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
        JsonBinding.OneStage<String> TITLE = identity("title", JSONObject::getString);
        JsonBinding.OneStage<String> DESCRIPTION = identity("description", JSONObject::getString);
        JsonBinding.TwoStage<String, URL> URL = simple("url", JSONObject::getString, Util::createUrl$rethrow);
        JsonBinding.TwoStage<String, Instant> TIMESTAMP = simple("timestamp", JSONObject::getString, Instant::parse);
        JsonBinding.TwoStage<Integer, Color> COLOR = simple("color", JSONObject::getInteger, Color::new);
        JsonBinding.TwoStage<JSONObject, Footer> FOOTER = serialize("footer", Footer.class);
        JsonBinding.TwoStage<JSONObject, Image> IMAGE = serialize("image", Image.class);
        JsonBinding.TwoStage<JSONObject, Thumbnail> THUMBNAIL = serialize("thumbnail", Thumbnail.class);
        JsonBinding.TwoStage<JSONObject, Video> VIDEO = serialize("video", Video.class);
        JsonBinding.TwoStage<JSONObject, Provider> PROVIDER = serialize("provider", Provider.class);
        JsonBinding.TwoStage<JSONObject, Author> AUTHOR = serialize("author", Author.class);
        JsonBinding.TriStage<JSONObject, Field> FIELDS = serializableCollection("fields", Field.class);
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
            JsonBinding.OneStage<String> TEXT = identity("text", JSONObject::getString);
            JsonBinding.TwoStage<String, URL> ICON_URL = simple("icon_url", JSONObject::getString, Util::createUrl$rethrow);
            JsonBinding.TwoStage<String, URL> PROXY_ICON_URL = simple("proxy_icon_url", JSONObject::getString, Util::createUrl$rethrow);
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
            JsonBinding.TwoStage<String, URL> URL = simple("url", JSONObject::getString, Util::createUrl$rethrow);
            JsonBinding.TwoStage<String, URL> PROXY_URL = simple("proxy_url", JSONObject::getString, Util::createUrl$rethrow);
            JsonBinding.OneStage<Integer> HEIGHT = identity("height", JSONObject::getInteger);
            JsonBinding.OneStage<Integer> WIDTH = identity("width", JSONObject::getInteger);
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
            JsonBinding.TwoStage<String, URL> URL = simple("url", JSONObject::getString, Util::createUrl$rethrow);
            JsonBinding.TwoStage<String, URL> PROXY_URL = simple("proxy_url", JSONObject::getString, Util::createUrl$rethrow);
            JsonBinding.OneStage<Integer> HEIGHT = identity("height", JSONObject::getInteger);
            JsonBinding.OneStage<Integer> WIDTH = identity("width", JSONObject::getInteger);
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
            JsonBinding.TwoStage<String, URL> URL = simple("url", JSONObject::getString, Util::createUrl$rethrow);
            JsonBinding.OneStage<Integer> HEIGHT = identity("height", JSONObject::getInteger);
            JsonBinding.OneStage<Integer> WIDTH = identity("width", JSONObject::getInteger);
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
            JsonBinding.OneStage<String> NAME = identity("name", JSONObject::getString);
            JsonBinding.TwoStage<String, URL> URL = simple("url", JSONObject::getString, Util::createUrl$rethrow);
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
            JsonBinding.OneStage<String> NAME = identity("name", JSONObject::getString);
            JsonBinding.TwoStage<String, URL> URL = simple("url", JSONObject::getString, Util::createUrl$rethrow);
            JsonBinding.TwoStage<String, URL> ICON_URL = simple("icon_url", JSONObject::getString, Util::createUrl$rethrow);
            JsonBinding.TwoStage<String, URL> PROXY_ICON_URL = simple("proxy_icon_url", JSONObject::getString, Util::createUrl$rethrow);
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
            JsonBinding.OneStage<String> NAME = identity("name", JSONObject::getString);
            JsonBinding.OneStage<String> VALUE = identity("value", JSONObject::getString);
            JsonBinding.OneStage<Boolean> INLINE = identity("inline", JSONObject::getBoolean);
        }
    }
}
