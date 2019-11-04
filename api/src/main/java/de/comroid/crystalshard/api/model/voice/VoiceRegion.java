package de.comroid.crystalshard.api.model.voice;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import de.comroid.crystalshard.adapter.Adapter;
import de.comroid.crystalshard.util.annotation.IntroducedBy;
import de.comroid.crystalshard.util.model.serialization.JsonBinding;
import de.comroid.crystalshard.util.model.serialization.JsonDeserializable;

import com.alibaba.fastjson.JSONObject;

import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;
import static de.comroid.crystalshard.util.model.serialization.JsonBinding.identity;

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
        JsonBinding.OneStage<String> ID = identity("id", JSONObject::getString);
        JsonBinding.OneStage<String> NAME = identity("name", JSONObject::getString);
        JsonBinding.OneStage<Boolean> VIP_ONLY = identity("vip", JSONObject::getBoolean);
        JsonBinding.OneStage<Boolean> OPTIMAL = identity("optimal", JSONObject::getBoolean);
        JsonBinding.OneStage<Boolean> DEPRECATED = identity("deprecated", JSONObject::getBoolean);
        JsonBinding.OneStage<Boolean> CUSTOM = identity("custom", JSONObject::getBoolean);
    }

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/voice#list-voice-regions")
    static CompletableFuture<Collection<VoiceRegion>> requestVoiceRegions() {
        return Adapter.staticOverride(VoiceRegion.class, "requestVoiceRegions");
    }
}
