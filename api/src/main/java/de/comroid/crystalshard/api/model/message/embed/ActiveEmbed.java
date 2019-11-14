package de.comroid.crystalshard.api.model.message.embed;

import java.awt.Color;
import java.net.URL;
import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

import de.comroid.crystalshard.util.Util;
import de.comroid.crystalshard.util.model.serialization.JSONBinding;
import de.comroid.crystalshard.util.model.serialization.JsonDeserializable;
import de.comroid.crystalshard.util.model.serialization.JSONBindingLocation;

import com.alibaba.fastjson.JSONObject;

import static de.comroid.crystalshard.util.model.serialization.JSONBinding.identity;
import static de.comroid.crystalshard.util.model.serialization.JSONBinding.serializableCollection;
import static de.comroid.crystalshard.util.model.serialization.JSONBinding.require;
import static de.comroid.crystalshard.util.model.serialization.JSONBinding.simple;

@JSONBindingLocation(ActiveEmbed.JSON.class)
public interface ActiveEmbed extends Embed, JsonDeserializable {
    @Override
    default Optional<String> getTitle() {
        return wrapBindingValue(JSON.TITLE);
    }

    @Override
    default Optional<String> getDescription() {
        return wrapBindingValue(JSON.DESCRIPTION);
    }

    @Override
    default Optional<URL> getURL() {
        return wrapBindingValue(JSON.URL);
    }

    @Override
    default Optional<Instant> getTimestamp() {
        return wrapBindingValue(JSON.TIMESTAMP);
    }

    @Override
    default Optional<Color> getColor() {
        return wrapBindingValue(JSON.COLOR);
    }

    @Override
    default Optional<Footer> getFooter() {
        return wrapBindingValue(JSON.FOOTER);
    }

    @Override
    default Optional<Image> getImage() {
        return wrapBindingValue(JSON.IMAGE);
    }

    @Override
    default Optional<Thumbnail> getThumbnail() {
        return wrapBindingValue(JSON.THUMBNAIL);
    }

    @Override
    default Optional<Video> getVideo() {
        return wrapBindingValue(JSON.VIDEO);
    }

    @Override
    default Optional<Provider> getProvider() {
        return wrapBindingValue(JSON.PROVIDER);
    }

    @Override
    default Optional<Author> getAuthor() {
        return wrapBindingValue(JSON.AUTHOR);
    }

    @Override
    default Collection<Field> getFields() {
        return getBindingValue(JSON.FIELDS);
    }

    interface JSON {
        JSONBinding.OneStage<String> TITLE = identity("title", JSONObject::getString);
        JSONBinding.OneStage<String> DESCRIPTION = identity("description", JSONObject::getString);
        JSONBinding.TwoStage<String, URL> URL = simple("url", JSONObject::getString, Util::createUrl$rethrow);
        JSONBinding.TwoStage<String, Instant> TIMESTAMP = simple("timestamp", JSONObject::getString, Instant::parse);
        JSONBinding.TwoStage<Integer, Color> COLOR = simple("color", JSONObject::getInteger, Color::new);
        JSONBinding.TwoStage<JSONObject, Footer> FOOTER = require("footer", Footer.class);
        JSONBinding.TwoStage<JSONObject, Image> IMAGE = require("image", Image.class);
        JSONBinding.TwoStage<JSONObject, Thumbnail> THUMBNAIL = require("thumbnail", Thumbnail.class);
        JSONBinding.TwoStage<JSONObject, Video> VIDEO = require("video", Video.class);
        JSONBinding.TwoStage<JSONObject, Provider> PROVIDER = require("provider", Provider.class);
        JSONBinding.TwoStage<JSONObject, Author> AUTHOR = require("author", Author.class);
        JSONBinding.TriStage<JSONObject, Field> FIELDS = serializableCollection("fields", Field.class);
    }

    @JSONBindingLocation(Footer.JSON.class)
    interface Footer extends Embed.Footer, JsonDeserializable {
        @Override
        default String getText() {
            return getBindingValue(JSON.TEXT);
        }

        @Override
        default Optional<URL> getIconURL() {
            return wrapBindingValue(JSON.ICON_URL);
        }

        @Override
        default Optional<URL> getProxyIconURL() {
            return wrapBindingValue(JSON.PROXY_ICON_URL);
        }

        interface JSON {
            JSONBinding.OneStage<String> TEXT = identity("text", JSONObject::getString);
            JSONBinding.TwoStage<String, URL> ICON_URL = simple("icon_url", JSONObject::getString, Util::createUrl$rethrow);
            JSONBinding.TwoStage<String, URL> PROXY_ICON_URL = simple("proxy_icon_url", JSONObject::getString, Util::createUrl$rethrow);
        }
    }

    @JSONBindingLocation(Footer.JSON.class)
    interface Image extends Embed.Image, JsonDeserializable {
        @Override
        default Optional<URL> getURL() {
            return wrapBindingValue(JSON.URL);
        }

        @Override
        default Optional<URL> getProxyURL() {
            return wrapBindingValue(JSON.PROXY_URL);
        }

        @Override
        default Optional<Integer> getHeight() {
            return wrapBindingValue(JSON.HEIGHT);
        }

        @Override
        default Optional<Integer> getWidth() {
            return wrapBindingValue(JSON.WIDTH);
        }

        interface JSON {
            JSONBinding.TwoStage<String, URL> URL = simple("url", JSONObject::getString, Util::createUrl$rethrow);
            JSONBinding.TwoStage<String, URL> PROXY_URL = simple("proxy_url", JSONObject::getString, Util::createUrl$rethrow);
            JSONBinding.OneStage<Integer> HEIGHT = identity("height", JSONObject::getInteger);
            JSONBinding.OneStage<Integer> WIDTH = identity("width", JSONObject::getInteger);
        }
    }

    @JSONBindingLocation(Footer.JSON.class)
    interface Thumbnail extends Embed.Thumbnail, JsonDeserializable {
        @Override
        default Optional<URL> getURL() {
            return wrapBindingValue(JSON.URL);
        }

        @Override
        default Optional<URL> getProxyURL() {
            return wrapBindingValue(JSON.PROXY_URL);
        }

        @Override
        default Optional<Integer> getHeight() {
            return wrapBindingValue(JSON.HEIGHT);
        }

        @Override
        default Optional<Integer> getWidth() {
            return wrapBindingValue(JSON.WIDTH);
        }

        interface JSON {
            JSONBinding.TwoStage<String, URL> URL = simple("url", JSONObject::getString, Util::createUrl$rethrow);
            JSONBinding.TwoStage<String, URL> PROXY_URL = simple("proxy_url", JSONObject::getString, Util::createUrl$rethrow);
            JSONBinding.OneStage<Integer> HEIGHT = identity("height", JSONObject::getInteger);
            JSONBinding.OneStage<Integer> WIDTH = identity("width", JSONObject::getInteger);
        }
    }

    @JSONBindingLocation(Footer.JSON.class)
    interface Video extends Embed.Video, JsonDeserializable {
        @Override
        default Optional<URL> getURL() {
            return wrapBindingValue(JSON.URL);
        }

        @Override
        default Optional<Integer> getHeight() {
            return wrapBindingValue(JSON.HEIGHT);
        }

        @Override
        default Optional<Integer> getWidth() {
            return wrapBindingValue(JSON.WIDTH);
        }

        interface JSON {
            JSONBinding.TwoStage<String, URL> URL = simple("url", JSONObject::getString, Util::createUrl$rethrow);
            JSONBinding.OneStage<Integer> HEIGHT = identity("height", JSONObject::getInteger);
            JSONBinding.OneStage<Integer> WIDTH = identity("width", JSONObject::getInteger);
        }
    }

    @JSONBindingLocation(Footer.JSON.class)
    interface Provider extends Embed.Provider, JsonDeserializable {
        @Override
        default Optional<String> getName() {
            return wrapBindingValue(JSON.NAME);
        }

        @Override
        default Optional<URL> getURL() {
            return wrapBindingValue(JSON.URL);
        }

        interface JSON {
            JSONBinding.OneStage<String> NAME = identity("name", JSONObject::getString);
            JSONBinding.TwoStage<String, URL> URL = simple("url", JSONObject::getString, Util::createUrl$rethrow);
        }
    }

    @JSONBindingLocation(Footer.JSON.class)
    interface Author extends Embed.Author, JsonDeserializable {
        @Override
        default Optional<String> getName() {
            return wrapBindingValue(JSON.NAME);
        }

        @Override
        default Optional<URL> getURL() {
            return wrapBindingValue(JSON.URL);
        }

        @Override
        default Optional<URL> getIconURL() {
            return wrapBindingValue(JSON.ICON_URL);
        }

        @Override
        default Optional<URL> getProxyIconURL() {
            return wrapBindingValue(JSON.PROXY_ICON_URL);
        }

        interface JSON {
            JSONBinding.OneStage<String> NAME = identity("name", JSONObject::getString);
            JSONBinding.TwoStage<String, URL> URL = simple("url", JSONObject::getString, Util::createUrl$rethrow);
            JSONBinding.TwoStage<String, URL> ICON_URL = simple("icon_url", JSONObject::getString, Util::createUrl$rethrow);
            JSONBinding.TwoStage<String, URL> PROXY_ICON_URL = simple("proxy_icon_url", JSONObject::getString, Util::createUrl$rethrow);
        }
    }

    @JSONBindingLocation(Footer.JSON.class)
    interface Field extends Embed.Field, JsonDeserializable {
        @Override 
        default String getName() {
            return getBindingValue(JSON.NAME);
        }

        @Override
        default String getValue() {
            return getBindingValue(JSON.VALUE);
        }

        @Override
        default boolean isInline() {
            return getBindingValue(JSON.INLINE);
        }

        interface JSON {
            JSONBinding.OneStage<String> NAME = identity("name", JSONObject::getString);
            JSONBinding.OneStage<String> VALUE = identity("value", JSONObject::getString);
            JSONBinding.OneStage<Boolean> INLINE = identity("inline", JSONObject::getBoolean);
        }
    }
}
