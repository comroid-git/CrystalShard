package de.comroid.crystalshard.api.entity.channel;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import de.comroid.crystalshard.adapter.Adapter;
import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.api.entity.EntityType;
import de.comroid.crystalshard.api.entity.Snowflake;
import de.comroid.crystalshard.api.event.channel.ChannelEvent;
import de.comroid.crystalshard.api.listener.ListenerSpec;
import de.comroid.crystalshard.api.listener.model.ListenerAttachable;
import de.comroid.crystalshard.api.model.Mentionable;
import de.comroid.crystalshard.api.model.channel.ChannelType;
import de.comroid.crystalshard.core.api.cache.Cacheable;
import de.comroid.crystalshard.core.api.rest.DiscordEndpoint;
import de.comroid.crystalshard.core.api.rest.RestMethod;
import de.comroid.crystalshard.util.annotation.IntroducedBy;
import de.comroid.crystalshard.util.model.TypeGroup;
import de.comroid.crystalshard.util.model.serialization.JSONBinding;

import com.alibaba.fastjson.JSONObject;

import static de.comroid.crystalshard.util.Util.hackCast;
import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;
import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.PRODUCTION;
import static de.comroid.crystalshard.util.model.serialization.JSONBinding.simple;

public interface Channel extends 
        Snowflake, 
        TypeGroup<Channel>, 
        Mentionable,
        ListenerAttachable<ListenerSpec.AttachableTo.Channel<? extends ChannelEvent<? extends Channel>>>,
        Cacheable {
    @IntroducedBy(API)
    default ChannelType getChannelType() {
        return getBindingValue(JSON.CHANNEL_TYPE);
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
                .executeAs(json -> Adapter.require(Channel.class, api, json));
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

    interface JSON extends Snowflake.Trait {
        JSONBinding.TwoStage<String, ChannelType> CHANNEL_TYPE = simple("type", JSONObject::getString, ChannelType::valueOf);
    }

    interface Default {
    }
}
