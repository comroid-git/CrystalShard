package de.comroid.crystalshard.api.model.message.reaction;

// https://discordapp.com/developers/docs/resources/channel#reaction-object-reaction-structure

import de.comroid.crystalshard.adapter.MainAPI;
import de.comroid.crystalshard.api.entity.emoji.Emoji;
import de.comroid.crystalshard.api.entity.message.Message;
import de.comroid.crystalshard.core.cache.Cacheable;
import de.comroid.crystalshard.util.annotation.IntroducedBy;
import de.comroid.crystalshard.util.model.serialization.JSONBinding;
import de.comroid.crystalshard.util.model.serialization.JsonDeserializable;
import de.comroid.crystalshard.util.model.serialization.JSONBindingLocation;

import com.alibaba.fastjson.JSONObject;

import static de.comroid.crystalshard.core.cache.Cacheable.makeSubcacheableInfo;
import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.GETTER;
import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.PRODUCTION;
import static de.comroid.crystalshard.util.model.serialization.JSONBinding.identity;
import static de.comroid.crystalshard.util.model.serialization.JSONBinding.require;

@MainAPI
@JSONBindingLocation(Reaction.JSON.class)
public interface Reaction extends JsonDeserializable, Cacheable {
    @CacheInformation.Marker
    CacheInformation<Message> CACHE_INFORMATION = makeSubcacheableInfo(Message.class, Reaction::getMessage);
    
    @IntroducedBy(GETTER)
    default int getCount() {
        return getBindingValue(JSON.COUNT);
    }

    @IntroducedBy(GETTER)
    default boolean haveYouReacted() {
        return getBindingValue(JSON.ME);
    }

    @IntroducedBy(GETTER)
    default Emoji getEmoji() {
        return getBindingValue(JSON.EMOJI);
    }
    
    @IntroducedBy(PRODUCTION)
    Message getMessage();

    interface JSON {
        JSONBinding.OneStage<Integer> COUNT = identity("count", JSONObject::getInteger);
        JSONBinding.OneStage<Boolean> ME = identity("me", JSONObject::getBoolean);
        JSONBinding.TwoStage<JSONObject, Emoji> EMOJI = require("emoji", Emoji.class);
    }
}
