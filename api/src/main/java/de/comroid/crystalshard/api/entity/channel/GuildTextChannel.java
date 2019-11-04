package de.comroid.crystalshard.api.entity.channel;

import java.time.Duration;
import java.util.Collection;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import de.comroid.crystalshard.adapter.Adapter;
import de.comroid.crystalshard.api.entity.EntityType;
import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.api.entity.guild.webhook.Webhook;
import de.comroid.crystalshard.api.model.channel.ChannelType;
import de.comroid.crystalshard.util.annotation.IntroducedBy;
import de.comroid.crystalshard.util.model.serialization.JsonBinding;
import de.comroid.crystalshard.util.model.serialization.JsonTraits;

import com.alibaba.fastjson.JSONObject;

import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.GETTER;
import static de.comroid.crystalshard.util.model.serialization.JsonBinding.identity;

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
        return Adapter.require(Builder.class, guild);
    }

    interface Trait extends GuildChannel.Trait, TextChannel.Trait {
        JsonBinding.OneStage<String> TOPIC = identity("topic", JSONObject::getString);
        JsonBinding.OneStage<Boolean> NSFW = identity("nsfw", JSONObject::getBoolean);
        JsonBinding.OneStage<Integer> MESSAGE_RATELIMIT = identity("rate_limit_per_user", JSONObject::getInteger);
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
