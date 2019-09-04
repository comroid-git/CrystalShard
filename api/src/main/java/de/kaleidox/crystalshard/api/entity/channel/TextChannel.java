package de.kaleidox.crystalshard.api.entity.channel;

import java.time.Duration;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import de.kaleidox.crystalshard.adapter.Adapter;
import de.kaleidox.crystalshard.api.entity.message.Message;
import de.kaleidox.crystalshard.api.model.message.Messageable;
import de.kaleidox.crystalshard.core.api.cache.CacheManager;
import de.kaleidox.crystalshard.core.api.rest.DiscordEndpoint;
import de.kaleidox.crystalshard.core.api.rest.RestMethod;
import de.kaleidox.crystalshard.util.annotation.IntroducedBy;

import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;

public interface TextChannel extends Channel, Messageable {
    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/channel#get-channel-message")
    default CompletableFuture<Message> requestMessage(long id) {
        return Adapter.<Message>request(getAPI())
                .endpoint(DiscordEndpoint.MESSAGE, getID(), id)
                .method(RestMethod.GET)
                .executeAs(data -> CacheManager.updateAndGet(Message.class, id, data));
    }

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/channel#trigger-typing-indicator")
    default CompletableFuture<Duration> triggerTypingIndicator() {
        return Adapter.<Duration>request(getAPI())
                .endpoint(DiscordEndpoint.CHANNEL_TYPING, getID())
                .method(RestMethod.POST)
                .expectCode(204)
                .executeAs(data -> Duration.ofSeconds(5));
    }

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/channel#get-pinned-messages")
    CompletableFuture<Collection<Message>> requestPinnedMessages();

    interface Builder<R extends TextChannel, Self extends Builder> extends Channel.Builder<R, Self> {
    }

    interface Updater<R extends TextChannel, Self extends Updater> extends Channel.Updater<R, Self> {
    }
}
