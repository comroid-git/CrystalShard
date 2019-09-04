package de.kaleidox.crystalshard.api.entity.channel;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import de.kaleidox.crystalshard.adapter.Adapter;
import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.entity.EntityType;
import de.kaleidox.crystalshard.api.entity.Snowflake;
import de.kaleidox.crystalshard.api.model.Mentionable;
import de.kaleidox.crystalshard.api.model.channel.ChannelType;
import de.kaleidox.crystalshard.core.api.cache.CacheManager;
import de.kaleidox.crystalshard.core.api.rest.DiscordEndpoint;
import de.kaleidox.crystalshard.core.api.rest.RestMethod;
import de.kaleidox.crystalshard.util.annotation.IntroducedBy;
import de.kaleidox.crystalshard.util.model.TypeGroup;

import static de.kaleidox.crystalshard.util.Util.hackCast;
import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;
import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.PRODUCTION;

public interface Channel extends Snowflake, TypeGroup<Channel>, Mentionable {
    @IntroducedBy(API)
    ChannelType getChannelType();

    @Override
    default EntityType getEntityType() {
        return EntityType.CHANNEL;
    }

    @IntroducedBy(API)
    default CompletableFuture<Void> delete() {
        return Adapter.<Void>request(getAPI())
                .endpoint(DiscordEndpoint.CHANNEL, getID())
                .method(RestMethod.DELETE)
                .executeAs(data -> CacheManager.delete(Channel.class, getID()));
    }

    @IntroducedBy(PRODUCTION)
    default Optional<TextChannel> asTextChannel() {
        return as(TextChannel.class);
    }

    @IntroducedBy(PRODUCTION)
    default Optional<VoiceChannel> asVoiceChannel() {
        return as(VoiceChannel.class);
    }

    @IntroducedBy(PRODUCTION)
    default Optional<GuildChannel> asServerChannel() {
        return as(GuildChannel.class);
    }

    @IntroducedBy(PRODUCTION)
    default Optional<GuildTextChannel> asServerTextChannel() {
        return as(GuildTextChannel.class);
    }

    @IntroducedBy(PRODUCTION)
    default Optional<GuildVoiceChannel> asServerVoiceChannel() {
        return as(GuildVoiceChannel.class);
    }

    @IntroducedBy(PRODUCTION)
    default Optional<GuildNewsChannel> asServerNewsChannel() {
        return as(GuildNewsChannel.class);
    }

    @IntroducedBy(PRODUCTION)
    default Optional<GuildStoreChannel> asServerStoreChannel() {
        return as(GuildStoreChannel.class);
    }

    @IntroducedBy(PRODUCTION)
    default Optional<GuildChannelCategory> asServerChannelCategory() {
        return as(GuildChannelCategory.class);
    }

    @IntroducedBy(PRODUCTION)
    default Optional<PrivateTextChannel> asPrivateTextChannel() {
        return as(PrivateTextChannel.class);
    }

    @IntroducedBy(PRODUCTION)
    default Optional<GroupTextChannel> asGroupTextChannel() {
        return as(GroupTextChannel.class);
    }

    @IntroducedBy(API)
    static CompletableFuture<Channel> requestChannel(Discord api, long id) {
        return Adapter.<Channel>request(api)
                .endpoint(DiscordEndpoint.CHANNEL, id)
                .method(RestMethod.GET)
                .executeAs(data -> CacheManager.updateAndGet(Channel.class, id, data));
    }

    interface Builder<R extends Channel, Self extends Channel.Builder> extends TypeGroup<Builder<R, Self>> {
        CompletableFuture<R> build();

        default Optional<TextChannel.Builder> asTextChannelBuilder() {
            return hackCast(as(hackCast(TextChannel.Builder.class)));
        }

        default Optional<VoiceChannel.Builder> asVoiceChannelBuilder() {
            return hackCast(as(hackCast(VoiceChannel.Builder.class)));
        }

        default Optional<GuildChannel.Builder> asServerChannelBuilder() {
            return hackCast(as(hackCast(GuildChannel.Builder.class)));
        }

        default Optional<GuildTextChannel.Builder> asServerTextChannelBuilder() {
            return hackCast(as(hackCast(GuildTextChannel.Builder.class)));
        }

        default Optional<GuildVoiceChannel.Builder> asServerVoiceChannelBuilder() {
            return hackCast(as(hackCast(GuildVoiceChannel.Builder.class)));
        }

        default Optional<GuildNewsChannel.Builder> asServerNewsChannelBuilder() {
            return hackCast(as(hackCast(GuildNewsChannel.Builder.class)));
        }

        default Optional<GuildStoreChannel.Builder> asServerStoreChannelBuilder() {
            return hackCast(as(hackCast(GuildStoreChannel.Builder.class)));
        }

        default Optional<GuildChannelCategory.Builder> asServerChannelCategoryBuilder() {
            return hackCast(as(hackCast(GuildChannelCategory.Builder.class)));
        }

        default Optional<PrivateTextChannel.Builder> asPrivateTextChannelBuilder() {
            return hackCast(as(hackCast(PrivateTextChannel.Builder.class)));
        }

        default Optional<GroupTextChannel.Builder> asGroupTextChannelBuilder() {
            return hackCast(as(hackCast(GroupTextChannel.Builder.class)));
        }
    }

    interface Updater<R extends Channel, Self extends Channel.Updater> extends TypeGroup<Updater<R, Self>> {
        @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/channel#modify-channel")
        CompletableFuture<R> update();

        default Optional<TextChannel.Updater> asTextChannelUpdater() {
            return hackCast(as(hackCast(TextChannel.Builder.class)));
        }

        default Optional<VoiceChannel.Updater> asVoiceChannelUpdater() {
            return hackCast(as(hackCast(VoiceChannel.Builder.class)));
        }

        default Optional<GuildChannel.Updater> asServerChannelUpdater() {
            return hackCast(as(hackCast(GuildChannel.Builder.class)));
        }

        default Optional<GuildTextChannel.Updater> asServerTextChannelUpdater() {
            return hackCast(as(hackCast(GuildTextChannel.Builder.class)));
        }

        default Optional<GuildVoiceChannel.Updater> asServerVoiceChannelUpdater() {
            return hackCast(as(hackCast(GuildVoiceChannel.Builder.class)));
        }

        default Optional<GuildNewsChannel.Updater> asServerNewsChannelUpdater() {
            return hackCast(as(hackCast(GuildNewsChannel.Updater.class)));
        }

        default Optional<GuildStoreChannel.Updater> asServerStoreChannelUpdater() {
            return hackCast(as(hackCast(GuildStoreChannel.Updater.class)));
        }

        default Optional<PrivateTextChannel.Updater> asPrivateTextChannelUpdater() {
            return hackCast(as(hackCast(PrivateTextChannel.Builder.class)));
        }

        default Optional<GroupTextChannel.Updater> asGroupTextChannelUpdater() {
            return hackCast(as(hackCast(GroupTextChannel.Builder.class)));
        }

        default Optional<GuildChannelCategory.Updater> asServerChannelCategoryUpdater() {
            return hackCast(as(hackCast(GuildChannelCategory.Builder.class)));
        }
    }
}
