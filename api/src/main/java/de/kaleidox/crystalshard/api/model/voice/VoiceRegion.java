package de.kaleidox.crystalshard.api.model.voice;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import de.kaleidox.crystalshard.adapter.Adapter;
import de.kaleidox.crystalshard.api.model.ApiBound;
import de.kaleidox.crystalshard.core.api.cache.CacheManager;
import de.kaleidox.crystalshard.util.annotation.IntroducedBy;
import de.kaleidox.crystalshard.util.model.serialization.JsonDeserializable;
import de.kaleidox.crystalshard.util.model.serialization.JsonTrait;

import com.fasterxml.jackson.databind.JsonNode;

import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;
import static de.kaleidox.crystalshard.util.model.serialization.JsonTrait.identity;

public interface VoiceRegion extends JsonDeserializable {
    default String getID() {
        return getTraitValue(Trait.ID);
    }

    default String getName() {
        return getTraitValue(Trait.NAME);
    }

    default boolean isVIPonly() {
        return getTraitValue(Trait.VIP_ONLY);
    }

    default boolean isOptimal() {
        return getTraitValue(Trait.OPTIMAL);
    }

    default boolean isDeprecated() {
        return getTraitValue(Trait.DEPRECATED);
    }

    default boolean isCustom() {
        return getTraitValue(Trait.CUSTOM);
    }

    interface Trait {
        JsonTrait<String, String> ID = identity(JsonNode::asText, "id");
        JsonTrait<String, String> NAME = identity(JsonNode::asText, "name");
        JsonTrait<Boolean, Boolean> VIP_ONLY = identity(JsonNode::asBoolean, "vip");
        JsonTrait<Boolean, Boolean> OPTIMAL = identity(JsonNode::asBoolean, "optimal");
        JsonTrait<Boolean, Boolean> DEPRECATED = identity(JsonNode::asBoolean, "deprecated");
        JsonTrait<Boolean, Boolean> CUSTOM = identity(JsonNode::asBoolean, "custom");
    }

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/voice#list-voice-regions")
    static CompletableFuture<Collection<VoiceRegion>> requestVoiceRegions() {
        return Adapter.staticOverride(VoiceRegion.class, "requestVoiceRegions");
    }
}
