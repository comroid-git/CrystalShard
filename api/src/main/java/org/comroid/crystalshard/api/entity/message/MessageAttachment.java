package org.comroid.crystalshard.api.entity.message;

import java.net.URL;
import java.util.Optional;

import org.comroid.crystalshard.adapter.MainAPI;
import org.comroid.crystalshard.api.entity.Snowflake;
import org.comroid.crystalshard.api.entity.message.MessageAttachment.Bind;
import org.comroid.crystalshard.util.Util;
import org.comroid.crystalshard.util.model.serialization.JSONBinding;
import org.comroid.crystalshard.util.model.serialization.JSONBindingLocation;

import com.alibaba.fastjson.JSONObject;

import static org.comroid.crystalshard.util.model.serialization.JSONBinding.identity;
import static org.comroid.crystalshard.util.model.serialization.JSONBinding.simple;

@MainAPI
@JSONBindingLocation(Bind.class)
public interface MessageAttachment extends Snowflake {
    default String getFilename() {
        return getBindingValue(Bind.FILENAME);
    }

    default int getFilesize() {
        return getBindingValue(Bind.SIZE);
    }

    default URL getURL() {
        return getBindingValue(Bind.URL);
    }

    default URL getProxyURL() {
        return getBindingValue(Bind.PROXY_URL);
    }

    default Optional<Integer> getHeight() {
        return wrapBindingValue(Bind.HEIGHT);
    }

    default Optional<Integer> getWidth() {
        return wrapBindingValue(Bind.WIDTH);
    }

    interface Bind extends Snowflake.Bind {
        JSONBinding.OneStage<String> FILENAME = identity("filename", JSONObject::getString);
        JSONBinding.OneStage<Integer> SIZE = identity("size", JSONObject::getInteger);
        JSONBinding.TwoStage<String, URL> URL = simple("url", JSONObject::getString, Util::createUrl$rethrow);
        JSONBinding.TwoStage<String, URL> PROXY_URL = simple("proxy_url", JSONObject::getString, Util::createUrl$rethrow);
        JSONBinding.OneStage<Integer> HEIGHT = identity("height", JSONObject::getInteger);
        JSONBinding.OneStage<Integer> WIDTH = identity("width", JSONObject::getInteger);
    }
}
