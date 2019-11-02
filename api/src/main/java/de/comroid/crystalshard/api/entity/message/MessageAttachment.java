package de.comroid.crystalshard.api.entity.message;

import java.net.URL;
import java.util.Optional;

import de.comroid.crystalshard.api.entity.Snowflake;
import de.comroid.crystalshard.util.Util;
import de.comroid.crystalshard.util.model.serialization.JsonBinding;
import de.comroid.crystalshard.util.model.serialization.JsonTraits;

import com.fasterxml.jackson.databind.JsonNode;

import static de.comroid.crystalshard.util.model.serialization.JsonBinding.identity;
import static de.comroid.crystalshard.util.model.serialization.JsonBinding.simple;

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
        JsonBinding<String, String> FILENAME = identity(JsonNode::asText, "filename");
        JsonBinding<Integer, Integer> SIZE = identity(JsonNode::asInt, "size");
        JsonBinding<String, URL> URL = simple(JsonNode::asText, "url", Util::createUrl$rethrow);
        JsonBinding<String, URL> PROXY_URL = simple(JsonNode::asText, "proxy_url", Util::createUrl$rethrow);
        JsonBinding<Integer, Integer> HEIGHT = identity(JsonNode::asInt, "height");
        JsonBinding<Integer, Integer> WIDTH = identity(JsonNode::asInt, "width");
    }
}
