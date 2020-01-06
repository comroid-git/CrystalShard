package org.comroid.crystalshard.api.model.voice;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import org.comroid.crystalshard.adapter.Adapter;
import org.comroid.crystalshard.util.annotation.IntroducedBy;
import org.comroid.crystalshard.util.model.serialization.JSONBinding;
import org.comroid.crystalshard.util.model.serialization.JsonDeserializable;

import com.alibaba.fastjson.JSONObject;

import static org.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;
import static org.comroid.crystalshard.util.model.serialization.JSONBinding.identity;

public interface VoiceRegion extends JsonDeserializable {
    default String getID() {
        return getBindingValue(JSON.ID);
    }

    default String getName() {
        return getBindingValue(JSON.NAME);
    }

    default boolean isVIPonly() {
        return getBindingValue(JSON.VIP_ONLY);
    }

    default boolean isOptimal() {
        return getBindingValue(JSON.OPTIMAL);
    }

    default boolean isDeprecated() {
        return getBindingValue(JSON.DEPRECATED);
    }

    default boolean isCustom() {
        return getBindingValue(JSON.CUSTOM);
    }

    interface JSON {
        JSONBinding.OneStage<String> ID = identity("id", JSONObject::getString);
        JSONBinding.OneStage<String> NAME = identity("name", JSONObject::getString);
        JSONBinding.OneStage<Boolean> VIP_ONLY = identity("vip", JSONObject::getBoolean);
        JSONBinding.OneStage<Boolean> OPTIMAL = identity("optimal", JSONObject::getBoolean);
        JSONBinding.OneStage<Boolean> DEPRECATED = identity("deprecated", JSONObject::getBoolean);
        JSONBinding.OneStage<Boolean> CUSTOM = identity("custom", JSONObject::getBoolean);
    }

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/voice#list-voice-regions")
    static CompletableFuture<Collection<VoiceRegion>> requestVoiceRegions() {
        return Adapter.staticOverride(VoiceRegion.class, "requestVoiceRegions");
    }
}
