package de.comroid.crystalshard.api.entity.channel;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;

import de.comroid.crystalshard.api.entity.EntityType;
import de.comroid.crystalshard.api.entity.user.User;
import de.comroid.crystalshard.core.api.cache.CacheManager;
import de.comroid.crystalshard.util.annotation.IntroducedBy;
import de.comroid.crystalshard.util.model.serialization.JsonBinding;

import com.fasterxml.jackson.databind.node.ArrayNode;

import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;
import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.GETTER;
import static de.comroid.crystalshard.util.model.serialization.JsonBinding.cache;
import static de.comroid.crystalshard.util.model.serialization.JsonBinding.underlyingCacheable;

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
        JsonBinding<ArrayNode, Collection<User>> RECIPIENTS = underlyingCacheable("recipients", User.class);
        JsonBinding<Long, User> USER_OWNER = cache("owner_id", CacheManager::getUserByID);
        JsonBinding<Long, User> APPLICATION_OWNER = cache("application_id", CacheManager::getUserByID);
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
