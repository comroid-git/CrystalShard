package de.comroid.crystalshard.api.entity.channel;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

import de.comroid.crystalshard.api.entity.Snowflake;
import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.api.model.guild.invite.Invite;
import de.comroid.crystalshard.api.model.permission.PermissionOverride;
import de.comroid.crystalshard.core.api.cache.CacheManager;
import de.comroid.crystalshard.util.annotation.IntroducedBy;
import de.comroid.crystalshard.util.model.serialization.JsonBinding;

import com.alibaba.fastjson.JSONObject;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;
import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.GETTER;
import static de.comroid.crystalshard.util.model.serialization.JsonBinding.cache;
import static de.comroid.crystalshard.util.model.serialization.JsonBinding.identity;
import static de.comroid.crystalshard.util.model.serialization.JsonBinding.serializableCollection;

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
        return getTraitValue(Trait.GUILD);
    }

    @IntroducedBy(GETTER)
    default int getPosition() {
        return getTraitValue(Trait.POSITION);
    }

    @IntroducedBy(GETTER)
    default Collection<PermissionOverride> getPermissionOverrides() {
        return getTraitValue(Trait.PERMISSION_OVERRIDES);
    }

    @IntroducedBy(GETTER)
    default String getName() {
        return getTraitValue(Trait.NAME);
    }

    @IntroducedBy(GETTER)
    default Optional<GuildChannelCategory> getCategory() {
        return wrapTraitValue(Trait.CATEGORY);
    }

    @IntroducedBy(API)
    CompletableFuture<Collection<Invite>> requestInvites();

    default Invite.Builder createInviteBuilder() {
        return Invite.builder(this);
    }

    interface Trait extends Channel.Trait {
        JsonBinding.TwoStage<Long, Guild> GUILD = cache("guild_id", CacheManager::getGuildByID);
        JsonBinding.OneStage<Integer> POSITION = identity("position", JSONObject::getInteger);
        JsonBinding.TriStage<JSONObject, PermissionOverride> PERMISSION_OVERRIDES = serializableCollection("permission_overwrites", PermissionOverride.class);
        JsonBinding.OneStage<String> NAME = identity("name", JSONObject::getString);
        JsonBinding.TwoStage<Long, GuildChannelCategory> CATEGORY = cache("parent_id",
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
