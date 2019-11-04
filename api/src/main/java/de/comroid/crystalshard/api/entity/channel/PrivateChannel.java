package de.comroid.crystalshard.api.entity.channel;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;

import de.comroid.crystalshard.api.entity.EntityType;
import de.comroid.crystalshard.api.entity.user.User;
import de.comroid.crystalshard.core.api.cache.CacheManager;
import de.comroid.crystalshard.util.annotation.IntroducedBy;
import de.comroid.crystalshard.util.model.serialization.JSONBinding;

import com.alibaba.fastjson.JSONObject;

import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;
import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.GETTER;
import static de.comroid.crystalshard.util.model.serialization.JSONBinding.cache;
import static de.comroid.crystalshard.util.model.serialization.JSONBinding.serializableCollection;

public interface PrivateChannel extends Channel {
    @Override
    default EntityType getEntityType() {
        return EntityType.PRIVATE_CHANNEL;
    }

    @IntroducedBy(GETTER)
    default Collection<User> getRecipients() {
        return getBindingValue(JSON.RECIPIENTS);
    }
    
    @IntroducedBy(GETTER)
    default Optional<User> getUserOwner() {
        return wrapBindingValue(JSON.USER_OWNER);
    }
    
    @IntroducedBy(GETTER)
    default Optional<User> getApplicationOwner() {
        return wrapBindingValue(JSON.APPLICATION_OWNER);
    }
    
    interface JSON extends Channel.Trait {
        JSONBinding.TriStage<JSONObject, User> RECIPIENTS = serializableCollection("recipients", User.class);
        JSONBinding.TwoStage<Long, User> USER_OWNER = cache("owner_id", CacheManager::getUserByID);
        JSONBinding.TwoStage<Long, User> APPLICATION_OWNER = cache("application_id", CacheManager::getUserByID);
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
