package org.comroid.crystalshard.api.entity.channel;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.comroid.crystalshard.adapter.Adapter;
import org.comroid.crystalshard.api.entity.message.Message;
import org.comroid.crystalshard.api.model.message.Messageable;
import org.comroid.crystalshard.core.rest.DiscordEndpoint;
import org.comroid.crystalshard.core.rest.HTTPStatusCodes;
import org.comroid.crystalshard.core.rest.RestMethod;
import org.comroid.crystalshard.util.annotation.IntroducedBy;
import org.comroid.crystalshard.util.model.serialization.JSONBinding;

import com.alibaba.fastjson.JSONObject;

import static org.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;
import static org.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.GETTER;
import static org.comroid.crystalshard.util.model.serialization.JSONBinding.simple;

public interface TextChannel extends Channel, Messageable {
    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/channel#get-channel-message")
    default CompletableFuture<Message> requestMessage(long id) {
        return Adapter.<Message>request(getAPI())
                .endpoint(DiscordEndpoint.MESSAGE, getID(), id)
                .method(RestMethod.GET)
                .executeAsObject(data -> Adapter.require(Message.class, getAPI(), data));
    }

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/channel#trigger-typing-indicator")
    default CompletableFuture<Duration> triggerTypingIndicator() {
        return Adapter.<Duration>request(getAPI())
                .endpoint(DiscordEndpoint.CHANNEL_TYPING, getID())
                .method(RestMethod.POST)
                .expectCode(HTTPStatusCodes.NO_CONTENT)
                .executeAsObject(data -> Duration.ofSeconds(5));
    }

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/channel#get-pinned-messages")
    CompletableFuture<Collection<Message>> requestPinnedMessages();
    
    @IntroducedBy(GETTER)
    default Optional<Instant> getLastPinnedTimestamp() {
        return wrapBindingValue(JSON.LAST_PINNED_TIMESTAMP);
    }
    
    @IntroducedBy(GETTER)
    default Optional<Message> getLastMessage() {
        return wrapBindingValue(JSON.LAST_MESSAGE_ID);
    }
    
    interface JSON extends Channel.JSON {
        JSONBinding.TwoStage<String, Instant> LAST_PINNED_TIMESTAMP = simple("last_pin_timestamp", JSONObject::getString, Instant::parse);
        JSONBinding.TwoStage<Long, Message> LAST_MESSAGE_ID = JSONBinding.cache("last_message_id", (cacheManager, id) -> cacheManager.getByID(Message.class, id));
    }

    interface Builder<R extends TextChannel, Self extends Builder> extends Channel.Builder<R, Self> {
    }

    interface Updater<R extends TextChannel, Self extends Updater> extends Channel.Updater<R, Self> {
    }
}
