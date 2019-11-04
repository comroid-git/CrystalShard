package de.comroid.crystalshard.api.model.message.reaction;

// https://discordapp.com/developers/docs/resources/channel#reaction-object-reaction-structure

import de.comroid.crystalshard.adapter.MainAPI;
import de.comroid.crystalshard.api.entity.emoji.Emoji;
import de.comroid.crystalshard.api.entity.message.Message;
import de.comroid.crystalshard.core.api.cache.Cacheable;
import de.comroid.crystalshard.util.annotation.IntroducedBy;
import de.comroid.crystalshard.util.model.serialization.JsonBinding;
import de.comroid.crystalshard.util.model.serialization.JsonDeserializable;
import de.comroid.crystalshard.util.model.serialization.JsonTraits;

import com.alibaba.fastjson.JSONObject;

import static de.comroid.crystalshard.core.api.cache.Cacheable.makeSubcacheableInfo;
import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.GETTER;
import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.PRODUCTION;
import static de.comroid.crystalshard.util.model.serialization.JsonBinding.identity;
import static de.comroid.crystalshard.util.model.serialization.JsonBinding.serialize;

@MainAPI
@JsonTraits(Reaction.Trait.class)
public interface Reaction extends JsonDeserializable, Cacheable {
    @CacheInformation.Marker
    CacheInformation<Message> CACHE_INFORMATION = makeSubcacheableInfo(Message.class, Reaction::getMessage);
    
    @IntroducedBy(GETTER)
    default int getCount() {
        return getTraitValue(Trait.COUNT);
    }

    @IntroducedBy(GETTER)
    default boolean haveYouReacted() {
        return getTraitValue(Trait.ME);
    }

    @IntroducedBy(GETTER)
    default Emoji getEmoji() {
        return getTraitValue(Trait.EMOJI);
    }
    
    @IntroducedBy(PRODUCTION)
    Message getMessage();

    interface Trait {
        JsonBinding.OneStage<Integer> COUNT = identity("count", JSONObject::getInteger);
        JsonBinding.OneStage<Boolean> ME = identity("me", JSONObject::getBoolean);
        JsonBinding.TwoStage<JSONObject, Emoji> EMOJI = serialize("emoji", Emoji.class);
    }
}
