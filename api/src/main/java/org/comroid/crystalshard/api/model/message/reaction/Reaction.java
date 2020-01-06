package org.comroid.crystalshard.api.model.message.reaction;

// https://discordapp.com/developers/docs/resources/channel#reaction-object-reaction-structure

import java.util.concurrent.CompletableFuture;

import org.comroid.crystalshard.adapter.MainAPI;
import org.comroid.crystalshard.api.entity.emoji.Emoji;
import org.comroid.crystalshard.api.entity.message.Message;
import org.comroid.crystalshard.api.entity.user.User;
import org.comroid.crystalshard.core.cache.Cacheable;
import org.comroid.crystalshard.util.annotation.IntroducedBy;
import org.comroid.crystalshard.util.model.serialization.JSONBinding;
import org.comroid.crystalshard.util.model.serialization.JsonDeserializable;
import org.comroid.crystalshard.util.model.serialization.JSONBindingLocation;

import com.alibaba.fastjson.JSONObject;

import static org.comroid.crystalshard.core.cache.Cacheable.makeSubcacheableInfo;
import static org.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.GETTER;
import static org.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.PRODUCTION;
import static org.comroid.crystalshard.util.model.serialization.JSONBinding.identity;
import static org.comroid.crystalshard.util.model.serialization.JSONBinding.require;

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

    interface Remover { // todo
        Remover byEmoji(String... emojis);
        
        Remover byEmoji(Emoji... emojis);
        
        Remover byAllEmojis();
        
        Remover byUser(User... users);
        
        Remover byEveryone();
        
        Remover byYourself();
        
        CompletableFuture<Void> remove();
    }
}
