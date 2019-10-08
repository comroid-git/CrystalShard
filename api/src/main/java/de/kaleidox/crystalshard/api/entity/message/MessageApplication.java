package de.kaleidox.crystalshard.api.entity.message;

// https://discordapp.com/developers/docs/resources/channel#message-object-message-application-structure

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

import de.kaleidox.crystalshard.api.entity.Snowflake;
import de.kaleidox.crystalshard.util.model.serialization.JsonTrait;
import de.kaleidox.crystalshard.util.model.serialization.JsonTraits;

import com.fasterxml.jackson.databind.JsonNode;

import static de.kaleidox.crystalshard.util.model.serialization.JsonTrait.identity;
import static de.kaleidox.crystalshard.util.model.serialization.JsonTrait.simple;

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
        JsonTrait<String, URL> COVER_IMAGE_URL = simple(JsonNode::asText, "cover_image", spec -> {
            try {
                return new URL(spec);
            } catch (MalformedURLException e) {
                throw new AssertionError(e);
            } // Todo use imagehelper?
        });
        JsonTrait<String, String> DESCRIPTION = identity(JsonNode::asText, "description");
        JsonTrait<String, URL> ICON_URL = simple(JsonNode::asText, "icon", spec -> {
            try {
                return new URL(spec);
            } catch (MalformedURLException e) {
                throw new AssertionError(e);
            } // Todo use imagehelper?
        });
        JsonTrait<String, String> NAME = identity(JsonNode::asText, "name");
    }
}
