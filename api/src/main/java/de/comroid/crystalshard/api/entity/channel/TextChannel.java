package de.comroid.crystalshard.api.entity.channel;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import de.comroid.crystalshard.adapter.Adapter;
import de.comroid.crystalshard.api.entity.message.Message;
import de.comroid.crystalshard.api.model.message.Messageable;
import de.comroid.crystalshard.core.api.rest.DiscordEndpoint;
import de.comroid.crystalshard.core.api.rest.HTTPStatusCodes;
import de.comroid.crystalshard.core.api.rest.RestMethod;
import de.comroid.crystalshard.util.annotation.IntroducedBy;
import de.comroid.crystalshard.util.model.serialization.JsonTrait;

import com.fasterxml.jackson.databind.JsonNode;

import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;
import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.GETTER;
import static de.comroid.crystalshard.util.model.serialization.JsonTrait.simple;

public interface TextChannel extends Channel, Messageable {
    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/channel#get-channel-message")
    default CompletableFuture<Message> requestMessage(long id) {
        return Adapter.<Message>request(getAPI())
                .endpoint(DiscordEndpoint.MESSAGE, getID(), id)
                .method(RestMethod.GET)
                .executeAs(data -> getAPI().getCacheManager()
                        .updateOrCreateAndGet(Message.class, id, data));
    }

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/channel#trigger-typing-indicator")
    default CompletableFuture<Duration> triggerTypingIndicator() {
        return Adapter.<Duration>request(getAPI())
                .endpoint(DiscordEndpoint.CHANNEL_TYPING, getID())
                .method(RestMethod.POST)
                .expectCode(HTTPStatusCodes.NO_CONTENT)
                .executeAs(data -> Duration.ofSeconds(5));
    }

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/channel#get-pinned-messages")
    CompletableFuture<Collection<Message>> requestPinnedMessages();
    
    @IntroducedBy(GETTER)
    default Optional<Instant> getLastPinnedTimestamp() {
        return wrapTraitValue(Trait.LAST_PINNED_TIMESTAMP);
    }
    
    @IntroducedBy(GETTER)
    default Optional<Message> getLastMessage() {
        return wrapTraitValue(Trait.LAST_MESSAGE_ID);
    }
    
    interface Trait extends Channel.Trait {
        JsonTrait<String, Instant> LAST_PINNED_TIMESTAMP = simple(JsonNode::asText, "last_pin_timestamp", Instant::parse);
        JsonTrait<Long, Message> LAST_MESSAGE_ID = JsonTrait.cache("last_message_id", (cacheManager, id) -> cacheManager.getByID(Message.class, id));
    }

    interface Builder<R extends TextChannel, Self extends Builder> extends Channel.Builder<R, Self> {
    }

    interface Updater<R extends TextChannel, Self extends Updater> extends Channel.Updater<R, Self> {
    }
}
