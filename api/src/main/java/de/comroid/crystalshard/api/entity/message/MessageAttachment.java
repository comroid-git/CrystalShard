package de.comroid.crystalshard.api.entity.message;

import java.net.URL;
import java.util.Optional;

import de.comroid.crystalshard.adapter.MainAPI;
import de.comroid.crystalshard.api.entity.Snowflake;
import de.comroid.crystalshard.util.Util;
import de.comroid.crystalshard.util.model.serialization.JsonBinding;
import de.comroid.crystalshard.util.model.serialization.JsonTraits;

import com.alibaba.fastjson.JSONObject;

import static de.comroid.crystalshard.util.model.serialization.JsonBinding.identity;
import static de.comroid.crystalshard.util.model.serialization.JsonBinding.simple;

@MainAPI
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
        JsonBinding.OneStage<String> FILENAME = identity("filename", JSONObject::getString);
        JsonBinding.OneStage<Integer> SIZE = identity("size", JSONObject::getInteger);
        JsonBinding.TwoStage<String, URL> URL = simple("url", JSONObject::getString, Util::createUrl$rethrow);
        JsonBinding.TwoStage<String, URL> PROXY_URL = simple("proxy_url", JSONObject::getString, Util::createUrl$rethrow);
        JsonBinding.OneStage<Integer> HEIGHT = identity("height", JSONObject::getInteger);
        JsonBinding.OneStage<Integer> WIDTH = identity("width", JSONObject::getInteger);
    }
}
