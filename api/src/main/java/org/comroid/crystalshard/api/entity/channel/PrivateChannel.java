package org.comroid.crystalshard.api.entity.channel;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;

import org.comroid.crystalshard.api.model.EntityType;
import org.comroid.crystalshard.api.entity.user.User;
import org.comroid.crystalshard.core.cache.CacheManager;
import org.comroid.crystalshard.util.annotation.IntroducedBy;
import org.comroid.crystalshard.util.model.serialization.JSONBinding;

import com.alibaba.fastjson.JSONObject;

import static org.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;
import static org.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.GETTER;
import static org.comroid.crystalshard.util.model.serialization.JSONBinding.cache;
import static org.comroid.crystalshard.util.model.serialization.JSONBinding.serializableCollection;

public interface PrivateChannel extends Channel {
    @Override
    default EntityType getEntityType() {
        return EntityType.PRIVATE_CHANNEL;
    }

    @IntroducedBy(GETTER)
    default Collection<User> getRecipients() {
        return getBindingValue(Bind.RECIPIENTS);
    }
    
    @IntroducedBy(GETTER)
    default Optional<User> getUserOwner() {
        return wrapBindingValue(Bind.USER_OWNER);
    }
    
    @IntroducedBy(GETTER)
    default Optional<User> getApplicationOwner() {
        return wrapBindingValue(Bind.APPLICATION_OWNER);
    }
    
    interface Bind extends Channel.Bind {
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
