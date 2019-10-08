package de.kaleidox.crystalshard.api.model.message.reaction;

// https://discordapp.com/developers/docs/resources/channel#reaction-object-reaction-structure

import java.util.Optional;

import de.kaleidox.crystalshard.api.entity.Snowflake;
import de.kaleidox.crystalshard.api.entity.emoji.Emoji;
import de.kaleidox.crystalshard.api.entity.message.Message;
import de.kaleidox.crystalshard.api.event.role.RoleEvent;
import de.kaleidox.crystalshard.api.listener.model.ListenerAttachable;
import de.kaleidox.crystalshard.api.listener.role.RoleAttachableListener;
import de.kaleidox.crystalshard.core.api.cache.Cacheable;
import de.kaleidox.crystalshard.util.annotation.IntroducedBy;
import de.kaleidox.crystalshard.util.model.serialization.JsonDeserializable;
import de.kaleidox.crystalshard.util.model.serialization.JsonTrait;
import de.kaleidox.crystalshard.util.model.serialization.JsonTraits;

import com.fasterxml.jackson.databind.JsonNode;

import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.GETTER;
import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.PRODUCTION;
import static de.kaleidox.crystalshard.util.model.serialization.JsonTrait.identity;
import static de.kaleidox.crystalshard.util.model.serialization.JsonTrait.underlying;

@JsonTraits(Reaction.Trait.class)
public interface Reaction extends JsonDeserializable, Cacheable, ListenerAttachable<RoleAttachableListener<? extends RoleEvent>> {
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
        JsonTrait<Integer, Integer> COUNT = identity(JsonNode::asInt, "count");
        JsonTrait<Boolean, Boolean> ME = identity(JsonNode::asBoolean, "me");
        JsonTrait<JsonNode, Emoji> EMOJI = underlying("emoji", Emoji.class);
    }

    @Override 
    default Optional<Long> getCacheParentID() {
        return Optional.of(getMessage().getID());
    }

    @Override
    default Optional<Class<? extends Cacheable>> getCacheParentType() {
        return Optional.of(Message.class);
    }

    @Override
    default boolean isSingletonType() {
        return false;
    }

    @Override
    default boolean isSubcacheMember() {
        return true;
    }
}
