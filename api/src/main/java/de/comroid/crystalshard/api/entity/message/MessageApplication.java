package de.comroid.crystalshard.api.entity.message;

// https://discordapp.com/developers/docs/resources/channel#message-object-message-application-structure

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

import de.comroid.crystalshard.api.entity.Snowflake;
import de.comroid.crystalshard.util.model.serialization.JsonBinding;
import de.comroid.crystalshard.util.model.serialization.JsonTraits;

import com.fasterxml.jackson.databind.JsonNode;

import static de.comroid.crystalshard.util.model.serialization.JsonBinding.identity;
import static de.comroid.crystalshard.util.model.serialization.JsonBinding.simple;

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
        JsonBinding<String, URL> COVER_IMAGE_URL = simple(JsonNode::asText, "cover_image", spec -> {
            try {
                return new URL(spec);
            } catch (MalformedURLException e) {
                throw new AssertionError(e);
            } // Todo use imagehelper?
        });
        JsonBinding<String, String> DESCRIPTION = identity(JsonNode::asText, "description");
        JsonBinding<String, URL> ICON_URL = simple(JsonNode::asText, "icon", spec -> {
            try {
                return new URL(spec);
            } catch (MalformedURLException e) {
                throw new AssertionError(e);
            } // Todo use imagehelper?
        });
        JsonBinding<String, String> NAME = identity(JsonNode::asText, "name");
    }
}
