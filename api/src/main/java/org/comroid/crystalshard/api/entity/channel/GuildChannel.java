package org.comroid.crystalshard.api.entity.channel;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

import org.comroid.crystalshard.api.entity.Snowflake;
import org.comroid.crystalshard.api.entity.guild.Guild;
import org.comroid.crystalshard.api.entity.user.User;
import org.comroid.crystalshard.api.model.guild.invite.Invite;
import org.comroid.crystalshard.api.model.permission.Permission;
import org.comroid.crystalshard.api.model.permission.PermissionOverride;
import org.comroid.crystalshard.core.cache.CacheManager;
import org.comroid.crystalshard.util.annotation.IntroducedBy;
import org.comroid.crystalshard.util.model.serialization.JSONBinding;

import com.alibaba.fastjson.JSONObject;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static org.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;
import static org.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.GETTER;
import static org.comroid.crystalshard.util.model.serialization.JSONBinding.cache;
import static org.comroid.crystalshard.util.model.serialization.JSONBinding.identity;
import static org.comroid.crystalshard.util.model.serialization.JSONBinding.serializableCollection;

public interface GuildChannel extends Channel {
    Comparator<GuildChannel> GUILD_CHANNEL_COMPARATOR = Comparator.comparingInt(GuildChannel::getPosition);

    @Override
    @Contract(pure = true)
    default int compareTo(@NotNull Snowflake other) {
        if (other instanceof GuildChannel)
            return GUILD_CHANNEL_COMPARATOR.compare(this, (GuildChannel) other);

        return ((Snowflake) this).compareTo(other);
    }

    @IntroducedBy(GETTER)
    default Guild getGuild() {
        return getBindingValue(Bind.GUILD);
    }

    @IntroducedBy(GETTER)
    default int getPosition() {
        return getBindingValue(Bind.POSITION);
    }

    @IntroducedBy(GETTER)
    default Collection<PermissionOverride> getPermissionOverrides() {
        return getBindingValue(Bind.PERMISSION_OVERRIDES);
    }

    @IntroducedBy(GETTER)
    default String getName() {
        return getBindingValue(Bind.NAME);
    }

    @IntroducedBy(GETTER)
    default Optional<GuildChannelCategory> getCategory() {
        return wrapBindingValue(Bind.CATEGORY);
    }

    @IntroducedBy(API)
    CompletableFuture<Collection<Invite>> requestInvites();

    default Invite.Builder createInviteBuilder() {
        return Invite.builder(this);
    }

    default boolean hasAnyPermission(User user, Permission[] targetPermissions) {
        return false; // todo
    }

    interface Bind extends Channel.Bind {
        JSONBinding.TwoStage<Long, Guild> GUILD = cache("guild_id", CacheManager::getGuildByID);
        JSONBinding.OneStage<Integer> POSITION = identity("position", JSONObject::getInteger);
        JSONBinding.TriStage<JSONObject, PermissionOverride> PERMISSION_OVERRIDES = serializableCollection("permission_overwrites", PermissionOverride.class);
        JSONBinding.OneStage<String> NAME = identity("name", JSONObject::getString);
        JSONBinding.TwoStage<Long, GuildChannelCategory> CATEGORY = cache("parent_id",
                (CacheManager cache, Long id) -> cache.getChannelByID(id).flatMap(Channel::asGuildChannelCategory));
    }

    interface Builder<R extends GuildChannel, Self extends GuildChannel.Builder> extends Channel.Builder<R, Self> {
        Guild getGuild();

        OptionalInt getPosition();

        Self setPosition(int position);

        @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/channel#edit-channel-permissions")
        Optional<Collection<PermissionOverride>> getPermissionOverrides();

        @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/channel#edit-channel-permissions")
        Self addPermissionOverride(PermissionOverride override);

        @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/channel#edit-channel-permissions")
        Self removePermissionOverrideIf(Predicate<PermissionOverride> tester);

        Optional<String> getName();

        Self setName(String name);

        Optional<GuildChannelCategory> getCategory();

        Self setGuildChannelCategory(GuildChannelCategory guildChannelCategory) throws IllegalArgumentException;

        @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/guild#create-guild-channel")
        @Override CompletableFuture<R> build();
    }

    interface Updater<R extends GuildChannel, Self extends GuildChannel.Updater> extends Channel.Updater<R, Self> {
        Guild getGuild();

        int getPosition();

        Self setPosition(int position);

        Collection<PermissionOverride> getPermissionOverrides();

        Self addPermissionOverride(PermissionOverride override);

        Self removePermissionOverrideIf(Predicate<PermissionOverride> tester);

        String getName();

        Self setName(String name);

        Optional<GuildChannelCategory> getCategory();

        Self setGuildChannelCategory(GuildChannelCategory guildChannelCategory) throws IllegalArgumentException;
    }
}
