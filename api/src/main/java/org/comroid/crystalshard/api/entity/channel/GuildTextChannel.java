package org.comroid.crystalshard.api.entity.channel;

import java.time.Duration;
import java.util.Collection;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.comroid.crystalshard.adapter.Adapter;
import org.comroid.crystalshard.api.model.EntityType;
import org.comroid.crystalshard.api.entity.guild.Guild;
import org.comroid.crystalshard.api.entity.guild.webhook.Webhook;
import org.comroid.crystalshard.api.model.channel.ChannelType;
import org.comroid.crystalshard.util.annotation.IntroducedBy;
import org.comroid.crystalshard.util.model.serialization.JSONBinding;
import org.comroid.crystalshard.util.model.serialization.JSONBindingLocation;

import com.alibaba.fastjson.JSONObject;

import static org.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.GETTER;
import static org.comroid.crystalshard.util.model.serialization.JSONBinding.identity;

@JSONBindingLocation(GuildTextChannel.Bind.class)
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
        return wrapBindingValue(Bind.TOPIC);
    }


    @IntroducedBy(GETTER)
    default boolean isNSFW() {
        return getBindingValue(Bind.NSFW);
    }
    
    @IntroducedBy(GETTER)
    default Optional<Integer> getMessageRatelimit() {
        return wrapBindingValue(Bind.MESSAGE_RATELIMIT);
    }

    Updater createUpdater();

    static Builder builder(Guild guild) {
        return Adapter.require(Builder.class, guild);
    }

    interface Bind extends Bind, TextChannel.Bind {
        JSONBinding.OneStage<String> TOPIC = identity("topic", JSONObject::getString);
        JSONBinding.OneStage<Boolean> NSFW = identity("nsfw", JSONObject::getBoolean);
        JSONBinding.OneStage<Integer> MESSAGE_RATELIMIT = identity("rate_limit_per_user", JSONObject::getInteger);
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
