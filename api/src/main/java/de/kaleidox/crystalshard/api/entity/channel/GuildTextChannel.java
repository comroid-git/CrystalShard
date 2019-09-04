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
import de.kaleidox.crystalshard.api.entity.user.Webhook;
import de.kaleidox.crystalshard.api.model.channel.ChannelType;

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

    Optional<String> getTopic();

    boolean isNSFW();

    OptionalInt getMessageRatelimit();

    Updater createUpdater();

    static Builder builder(Guild guild) {
        return Adapter.create(Builder.class, guild);
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
