package de.kaleidox.crystalshard.api.entity.channel;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import de.kaleidox.crystalshard.adapter.Adapter;
import de.kaleidox.crystalshard.api.entity.message.Message;
import de.kaleidox.crystalshard.api.model.message.Messageable;
import de.kaleidox.crystalshard.core.api.rest.DiscordEndpoint;
import de.kaleidox.crystalshard.core.api.rest.HTTPStatusCodes;
import de.kaleidox.crystalshard.core.api.rest.RestMethod;
import de.kaleidox.crystalshard.util.annotation.IntroducedBy;
import de.kaleidox.crystalshard.util.model.serialization.JsonTrait;

import com.fasterxml.jackson.databind.JsonNode;

import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;
import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.GETTER;
import static de.kaleidox.crystalshard.util.model.serialization.JsonTrait.simple;

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
        JsonTrait<Long, Message> LAST_MESSAGE_ID = JsonTrait.cache(JsonNode::asLong, "last_message_id",
                (cacheManager, id) -> cacheManager.getSnowflakesByID(id)
                        .stream()
                        .filter(Message.class::isInstance)
                        .findAny()
                        .map(Message.class::cast)
        );
    }

    interface Builder<R extends TextChannel, Self extends Builder> extends Channel.Builder<R, Self> {
    }

    interface Updater<R extends TextChannel, Self extends Updater> extends Channel.Updater<R, Self> {
    }
}
