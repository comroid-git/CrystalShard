package de.comroid.crystalshard.api.entity.message;

import java.net.URL;
import java.util.Optional;

import de.comroid.crystalshard.api.entity.Snowflake;
import de.comroid.crystalshard.util.Util;
import de.comroid.crystalshard.util.model.serialization.JsonTrait;
import de.comroid.crystalshard.util.model.serialization.JsonTraits;

import com.fasterxml.jackson.databind.JsonNode;

import static de.comroid.crystalshard.util.model.serialization.JsonTrait.identity;
import static de.comroid.crystalshard.util.model.serialization.JsonTrait.simple;

@JsonTraits(MessageAttachment.Trait.class)
public interface MessageAttachment extends Snowflake {
    default String getFilename() {
        return getTraitValue(Trait.FILENAME);
    }

    default int getFilesize() {
        return getTraitValue(Trait.SIZE);
    }

    default URL getURL() {
        return getTraitValue(Trait.URL);
    }

    default URL getProxyURL() {
        return getTraitValue(Trait.PROXY_URL);
    }

    default Optional<Integer> getHeight() {
        return wrapTraitValue(Trait.HEIGHT);
    }

    default Optional<Integer> getWidth() {
        return wrapTraitValue(Trait.WIDTH);
    }

    interface Trait extends Snowflake.Trait {
        JsonTrait<String, String> FILENAME = identity(JsonNode::asText, "filename");
        JsonTrait<Integer, Integer> SIZE = identity(JsonNode::asInt, "size");
        JsonTrait<String, URL> URL = simple(JsonNode::asText, "url", Util::url_rethrow);
        JsonTrait<String, URL> PROXY_URL = simple(JsonNode::asText, "proxy_url", Util::url_rethrow);
        JsonTrait<Integer, Integer> HEIGHT = identity(JsonNode::asInt, "height");
        JsonTrait<Integer, Integer> WIDTH = identity(JsonNode::asInt, "width");
    }
}
