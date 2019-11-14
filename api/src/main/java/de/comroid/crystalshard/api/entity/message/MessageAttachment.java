package de.comroid.crystalshard.api.entity.message;

import java.net.URL;
import java.util.Optional;

import de.comroid.crystalshard.adapter.MainAPI;
import de.comroid.crystalshard.api.entity.Snowflake;
import de.comroid.crystalshard.util.Util;
import de.comroid.crystalshard.util.model.serialization.JSONBinding;
import de.comroid.crystalshard.util.model.serialization.JSONBindingLocation;

import com.alibaba.fastjson.JSONObject;

import static de.comroid.crystalshard.util.model.serialization.JSONBinding.identity;
import static de.comroid.crystalshard.util.model.serialization.JSONBinding.simple;

@MainAPI
@JSONBindingLocation(MessageAttachment.JSON.class)
public interface MessageAttachment extends Snowflake {
    default String getFilename() {
        return getBindingValue(JSON.FILENAME);
    }

    default int getFilesize() {
        return getBindingValue(JSON.SIZE);
    }

    default URL getURL() {
        return getBindingValue(JSON.URL);
    }

    default URL getProxyURL() {
        return getBindingValue(JSON.PROXY_URL);
    }

    default Optional<Integer> getHeight() {
        return wrapBindingValue(JSON.HEIGHT);
    }

    default Optional<Integer> getWidth() {
        return wrapBindingValue(JSON.WIDTH);
    }

    interface JSON extends Snowflake.JSON {
        JSONBinding.OneStage<String> FILENAME = identity("filename", JSONObject::getString);
        JSONBinding.OneStage<Integer> SIZE = identity("size", JSONObject::getInteger);
        JSONBinding.TwoStage<String, URL> URL = simple("url", JSONObject::getString, Util::createUrl$rethrow);
        JSONBinding.TwoStage<String, URL> PROXY_URL = simple("proxy_url", JSONObject::getString, Util::createUrl$rethrow);
        JSONBinding.OneStage<Integer> HEIGHT = identity("height", JSONObject::getInteger);
        JSONBinding.OneStage<Integer> WIDTH = identity("width", JSONObject::getInteger);
    }
}
