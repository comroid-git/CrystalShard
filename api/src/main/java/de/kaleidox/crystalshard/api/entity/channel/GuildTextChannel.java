package de.kaleidox.crystalshard.api.entity.channel;

import java.time.Duration;
import java.util.Collection;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import de.kaleidox.crystalshard.adapter.Adapter;
import de.kaleidox.crystalshard.api.entity.EntityType;
import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.api.entity.guild.webhook.Webhook;
import de.kaleidox.crystalshard.api.model.channel.ChannelType;
import de.kaleidox.crystalshard.util.annotation.IntroducedBy;
import de.kaleidox.crystalshard.util.model.serialization.JsonTrait;
import de.kaleidox.crystalshard.util.model.serialization.JsonTraits;

import com.fasterxml.jackson.databind.JsonNode;

import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.GETTER;
import static de.kaleidox.crystalshard.util.model.serialization.JsonTrait.identity;
import static de.kaleidox.crystalshard.util.model.serialization.JsonTrait.simple;

@JsonTraits(GuildTextChannel.Trait.class)
public interface GuildTextChannel extends GuildChannel, TextChannel {
    // direct api methods
    CompletableFuture<Collection<Webhook>> requestWebhooks();

    // getters
    @Override
    default ChannelType getChannelType() {
        return ChannelType.GUILD_TEXT;
    }

    @Override
    default EntityType getEntityType() {
        return EntityType.GUILD_TEXT_CHANNEL;
    }

    @IntroducedBy(GETTER)
    default Optional<String> getTopic() {
        return wrapTraitValue(Trait.TOPIC);
    }


    @IntroducedBy(GETTER)
    default boolean isNSFW() {
        return getTraitValue(Trait.NSFW);
    }
    
    @IntroducedBy(GETTER)
    default Optional<Integer> getMessageRatelimit() {
        return wrapTraitValue(Trait.MESSAGE_RATELIMIT);
    }

    Updater createUpdater();

    static Builder builder(Guild guild) {
        return Adapter.create(Builder.class, guild);
    }

    interface Trait extends GuildChannel.Trait, TextChannel.Trait {
        JsonTrait<String, String> TOPIC = identity(JsonNode::asText, "topic");
        JsonTrait<Boolean, Boolean> NSFW = identity(JsonNode::asBoolean, "nsfw");
        JsonTrait<Integer, Integer> MESSAGE_RATELIMIT = identity(JsonNode::asInt, "rate_limit_per_user");
    }

    interface Builder extends
            GuildChannel.Builder<GuildTextChannel, Builder>,
            TextChannel.Builder<GuildTextChannel, Builder> {
        Optional<String> getTopic();

        Builder setTopic(String topic);

        boolean isNSFW();

        Builder setNSFW(boolean nsfw);

        OptionalInt getMessageRatelimit();

        Builder setMessageRatelimit(int messageRatelimit);
    }

    interface Updater extends
            GuildChannel.Updater<GuildTextChannel, Updater>,
            TextChannel.Updater<GuildTextChannel, Updater> {
        Optional<String> getTopic();

        Updater setTopic(String topic);

        boolean isNSFW();

        Updater setNSFW(boolean nsfw);

        Optional<Duration> getMessageRatelimit();

        Updater setMessageRatelimit(long time, TimeUnit unit);
    }
}
