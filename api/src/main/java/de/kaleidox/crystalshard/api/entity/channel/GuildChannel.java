package de.kaleidox.crystalshard.api.entity.channel;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

import de.kaleidox.crystalshard.api.entity.Snowflake;
import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.api.model.guild.invite.Invite;
import de.kaleidox.crystalshard.api.model.permission.PermissionOverride;
import de.kaleidox.crystalshard.core.api.cache.CacheManager;
import de.kaleidox.crystalshard.util.annotation.IntroducedBy;
import de.kaleidox.crystalshard.util.model.serialization.JsonTrait;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;
import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.GETTER;
import static de.kaleidox.crystalshard.util.model.serialization.JsonTrait.cache;
import static de.kaleidox.crystalshard.util.model.serialization.JsonTrait.collective;
import static de.kaleidox.crystalshard.util.model.serialization.JsonTrait.identity;

public interface GuildChannel extends Channel {
    Comparator<GuildChannel> GUILD_CHANNEL_COMPARATOR = Comparator.comparingInt(GuildChannel::getPosition);

    @Override
    @Contract(pure = true)
    default int compareTo(@NotNull Snowflake other) {
        if (other instanceof GuildChannel)
            return GUILDCHANNEL_COMPARATOR.compare(this, (GuildChannel) other);

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
        JsonTrait<Long, Guild> GUILD = cache("guild_id", CacheManager::getGuildByID);

        JsonTrait<Integer, Integer> POSITION = identity(JsonNode::asInt, "position");

        JsonTrait<ArrayNode, Collection<PermissionOverride>> PERMISSION_OVERRIDES = collective("permission_overwrites", PermissionOverride.class);

        JsonTrait<String, String> NAME = identity(JsonNode::asText, "name");

        JsonTrait<Long, GuildChannelCategory> CATEGORY = cache("parent_id",
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
