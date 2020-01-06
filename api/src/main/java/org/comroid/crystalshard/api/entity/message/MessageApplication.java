package org.comroid.crystalshard.api.entity.message;

// https://discordapp.com/developers/docs/resources/channel#message-object-message-application-structure

import java.net.URL;
import java.util.Optional;

import org.comroid.crystalshard.adapter.MainAPI;
import org.comroid.crystalshard.api.entity.Snowflake;
import org.comroid.crystalshard.util.Util;
import org.comroid.crystalshard.util.model.serialization.JSONBinding;
import org.comroid.crystalshard.util.model.serialization.JSONBindingLocation;

import com.alibaba.fastjson.JSONObject;

import static org.comroid.crystalshard.util.model.serialization.JSONBinding.identity;
import static org.comroid.crystalshard.util.model.serialization.JSONBinding.simple;

@MainAPI
@JSONBindingLocation(MessageApplication.JSON.class)
public interface MessageApplication extends Snowflake {
    default Optional<URL> getCoverImageURL() {
        return wrapBindingValue(JSON.COVER_IMAGE_URL);
    }

    default String getDescription() {
        return getBindingValue(JSON.DESCRIPTION);
    }

    default Optional<URL> getIconURL() {
        return wrapBindingValue(JSON.ICON_URL);
    }

    default String getName() {
        return getBindingValue(JSON.NAME);
    }

    interface JSON extends Snowflake.JSON {
        JSONBinding.TwoStage<String, URL> COVER_IMAGE_URL = simple("cover_image", JSONObject::getString, Util::createUrl$rethrow);
        JSONBinding.OneStage<String> DESCRIPTION = identity("description", JSONObject::getString);
        JSONBinding.TwoStage<String, URL> ICON_URL = simple("icon", JSONObject::getString, Util::createUrl$rethrow);
        JSONBinding.OneStage<String> NAME = identity("name", JSONObject::getString);
    }
}
