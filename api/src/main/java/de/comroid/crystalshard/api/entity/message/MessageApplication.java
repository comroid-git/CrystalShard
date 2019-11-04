package de.comroid.crystalshard.api.entity.message;

// https://discordapp.com/developers/docs/resources/channel#message-object-message-application-structure

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
@JsonTraits(MessageApplication.Trait.class)
public interface MessageApplication extends Snowflake {
    default Optional<URL> getCoverImageURL() {
        return wrapTraitValue(Trait.COVER_IMAGE_URL);
    }

    default String getDescription() {
        return getTraitValue(Trait.DESCRIPTION);
    }

    default Optional<URL> getIconURL() {
        return wrapTraitValue(Trait.ICON_URL);
    }

    default String getName() {
        return getTraitValue(Trait.NAME);
    }

    interface Trait extends Snowflake.Trait {
        JsonBinding.TwoStage<String, URL> COVER_IMAGE_URL = simple("cover_image", JSONObject::getString, Util::createUrl$rethrow);
        JsonBinding.OneStage<String> DESCRIPTION = identity("description", JSONObject::getString);
        JsonBinding.TwoStage<String, URL> ICON_URL = simple("icon", JSONObject::getString, Util::createUrl$rethrow);
        JsonBinding.OneStage<String> NAME = identity("name", JSONObject::getString);
    }
}
