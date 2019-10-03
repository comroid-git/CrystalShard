package de.kaleidox.crystalshard.api.entity.channel;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import de.kaleidox.crystalshard.adapter.Adapter;
import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.entity.EntityType;
import de.kaleidox.crystalshard.api.entity.Snowflake;
import de.kaleidox.crystalshard.api.listener.channel.ChannelAttachableListener;
import de.kaleidox.crystalshard.api.listener.model.ListenerAttachable;
import de.kaleidox.crystalshard.api.model.Mentionable;
import de.kaleidox.crystalshard.api.model.channel.ChannelType;
import de.kaleidox.crystalshard.core.api.cache.Cacheable;
import de.kaleidox.crystalshard.core.api.rest.DiscordEndpoint;
import de.kaleidox.crystalshard.core.api.rest.RestMethod;
import de.kaleidox.crystalshard.util.annotation.IntroducedBy;
import de.kaleidox.crystalshard.util.model.TypeGroup;
import de.kaleidox.crystalshard.util.model.serialization.JsonTrait;

import static de.kaleidox.crystalshard.util.Util.hackCast;
import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;
import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.PRODUCTION;
import static de.kaleidox.crystalshard.util.model.serialization.JsonTrait.simple;

public interface Channel extends Snowflake, TypeGroup<Channel>, Mentionable, ListenerAttachable<ChannelAttachableListener>, Cacheable {
    @IntroducedBy(API)
    default ChannelType getChannelType() {
        return getTraitValue(Trait.CHANNEL_TYPE);
    }

    @Override
    default EntityType getEntityType() {
        return EntityType.CHANNEL;
    }

    @IntroducedBy(API)
    default CompletableFuture<Void> delete() {
        return Adapter.<Void>request(getAPI())
                .endpoint(DiscordEndpoint.CHANNEL, getID())
                .method(RestMethod.DELETE)
                .executeAs(data -> getAPI().getCacheManager().delete(Channel.class, getID()));
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
    default Optional<GuildChannel> asGuildChannel() {
        return as(GuildChannel.class);
    }

    @IntroducedBy(PRODUCTION)
    default Optional<GuildTextChannel> asGuildTextChannel() {
        return as(GuildTextChannel.class);
    }

    @IntroducedBy(PRODUCTION)
    default Optional<GuildVoiceChannel> asGuildVoiceChannel() {
        return as(GuildVoiceChannel.class);
    }

    @IntroducedBy(PRODUCTION)
    default Optional<GuildNewsChannel> asGuildNewsChannel() {
        return as(GuildNewsChannel.class);
    }

    @IntroducedBy(PRODUCTION)
    default Optional<GuildStoreChannel> asGuildStoreChannel() {
        return as(GuildStoreChannel.class);
    }

    @IntroducedBy(PRODUCTION)
    default Optional<GuildChannelCategory> asGuildChannelCategory() {
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

    @Override
    default String getMentionTag() {
        return "<#" + getID() + ">";
    }

    @IntroducedBy(API)
    static CompletableFuture<Channel> requestChannel(Discord api, long id) {
        return Adapter.<Channel>request(api)
                .endpoint(DiscordEndpoint.CHANNEL, id)
                .method(RestMethod.GET)
                .executeAs(data -> api.getCacheManager()
                        .updateOrCreateAndGet(Channel.class, id, data));
    }

    interface Builder<R extends Channel, Self extends Channel.Builder> extends TypeGroup<Builder<R, Self>> {
        CompletableFuture<R> build();

        default Optional<TextChannel.Builder> asTextChannelBuilder() {
            return hackCast(as(hackCast(TextChannel.Builder.class)));
        }

        default Optional<VoiceChannel.Builder> asVoiceChannelBuilder() {
            return hackCast(as(hackCast(VoiceChannel.Builder.class)));
        }

        default Optional<GuildChannel.Builder> asGuildChannelBuilder() {
            return hackCast(as(hackCast(GuildChannel.Builder.class)));
        }

        default Optional<GuildTextChannel.Builder> asGuildTextChannelBuilder() {
            return hackCast(as(hackCast(GuildTextChannel.Builder.class)));
        }

        default Optional<GuildVoiceChannel.Builder> asGuildVoiceChannelBuilder() {
            return hackCast(as(hackCast(GuildVoiceChannel.Builder.class)));
        }

        default Optional<GuildNewsChannel.Builder> asGuildNewsChannelBuilder() {
            return hackCast(as(hackCast(GuildNewsChannel.Builder.class)));
        }

        default Optional<GuildStoreChannel.Builder> asGuildStoreChannelBuilder() {
            return hackCast(as(hackCast(GuildStoreChannel.Builder.class)));
        }

        default Optional<GuildChannelCategory.Builder> asGuildChannelCategoryBuilder() {
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

        default Optional<GuildChannel.Updater> asGuildChannelUpdater() {
            return hackCast(as(hackCast(GuildChannel.Builder.class)));
        }

        default Optional<GuildTextChannel.Updater> asGuildTextChannelUpdater() {
            return hackCast(as(hackCast(GuildTextChannel.Builder.class)));
        }

        default Optional<GuildVoiceChannel.Updater> asGuildVoiceChannelUpdater() {
            return hackCast(as(hackCast(GuildVoiceChannel.Builder.class)));
        }

        default Optional<GuildNewsChannel.Updater> asGuildNewsChannelUpdater() {
            return hackCast(as(hackCast(GuildNewsChannel.Updater.class)));
        }

        default Optional<GuildStoreChannel.Updater> asGuildStoreChannelUpdater() {
            return hackCast(as(hackCast(GuildStoreChannel.Updater.class)));
        }

        default Optional<PrivateTextChannel.Updater> asPrivateTextChannelUpdater() {
            return hackCast(as(hackCast(PrivateTextChannel.Builder.class)));
        }

        default Optional<GroupTextChannel.Updater> asGroupTextChannelUpdater() {
            return hackCast(as(hackCast(GroupTextChannel.Builder.class)));
        }

        default Optional<GuildChannelCategory.Updater> asGuildChannelCategoryUpdater() {
            return hackCast(as(hackCast(GuildChannelCategory.Builder.class)));
        }
    }

    interface Trait extends Snowflake.Trait {
        JsonTrait<String, ChannelType> CHANNEL_TYPE = simple("type", ChannelType::valueOf);
    }
}
