package de.kaleidox.crystalshard.api.entity.channel;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;

import de.kaleidox.crystalshard.api.entity.EntityType;
import de.kaleidox.crystalshard.api.entity.user.User;
import de.kaleidox.crystalshard.core.api.cache.CacheManager;
import de.kaleidox.crystalshard.util.annotation.IntroducedBy;
import de.kaleidox.crystalshard.util.model.serialization.JsonTrait;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;
import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.GETTER;
import static de.kaleidox.crystalshard.util.model.serialization.JsonTrait.cache;
import static de.kaleidox.crystalshard.util.model.serialization.JsonTrait.collective;

public interface PrivateChannel extends Channel {
    @Override
    default EntityType getEntityType() {
        return EntityType.PRIVATE_CHANNEL;
    }

    @IntroducedBy(GETTER)
    default Collection<User> getRecipients() {
        return getTraitValue(Trait.RECIPIENTS);
    }
    
    @IntroducedBy(GETTER)
    default Optional<User> getUserOwner() {
        return wrapTraitValue(Trait.USER_OWNER);
    }
    
    @IntroducedBy(GETTER)
    default Optional<User> getApplicationOwner() {
        return wrapTraitValue(Trait.APPLICATION_OWNER);
    }
    
    interface Trait extends Channel.Trait {
        JsonTrait<ArrayNode, Collection<User>> RECIPIENTS = collective("recipients", User.class);
        JsonTrait<Long, User> USER_OWNER = cache(JsonNode::asLong, "owner_id", CacheManager::getUserByID);
        JsonTrait<Long, User> APPLICATION_OWNER = cache(JsonNode::asLong, "application_id", CacheManager::getUserByID);
    }

    interface Builder<R extends PrivateChannel, Self extends PrivateChannel.Builder>
            extends Channel.Builder<R, Self> {
        Collection<User> getRecipients();

        @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/channel#group-dm-add-recipient")
        Self addRecipient(User user);

        @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/channel#group-dm-add-recipient")
        Self removeRecipientIf(Predicate<User> tester);
    }

    interface Updater<R extends PrivateChannel, Self extends PrivateChannel.Updater>
            extends Channel.Updater<R, Self> {
        Collection<User> getRecipients();

        @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/channel#group-dm-add-recipient")
        Self addRecipient(User user);

        @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/channel#group-dm-add-recipient")
        Self removeRecipientIf(Predicate<User> tester);
    }
}
