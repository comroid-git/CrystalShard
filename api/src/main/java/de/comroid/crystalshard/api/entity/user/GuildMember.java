package de.comroid.crystalshard.api.entity.user;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

import de.comroid.crystalshard.adapter.Adapter;
import de.comroid.crystalshard.api.entity.channel.GuildVoiceChannel;
import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.api.entity.guild.Role;
import de.comroid.crystalshard.api.model.guild.ban.Ban;
import de.comroid.crystalshard.api.model.permission.PermissionOverridable;
import de.comroid.crystalshard.core.api.rest.DiscordEndpoint;
import de.comroid.crystalshard.core.api.rest.HTTPStatusCodes;
import de.comroid.crystalshard.core.api.rest.RestMethod;
import de.comroid.crystalshard.util.annotation.IntroducedBy;
import de.comroid.crystalshard.util.model.serialization.JsonDeserializable;
import de.comroid.crystalshard.util.model.serialization.JsonTrait;
import de.comroid.crystalshard.util.model.serialization.JsonTraits;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.jetbrains.annotations.Nullable;

import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;
import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.PRODUCTION;
import static de.comroid.crystalshard.util.model.serialization.JsonTrait.identity;
import static de.comroid.crystalshard.util.model.serialization.JsonTrait.simple;
import static de.comroid.crystalshard.util.model.serialization.JsonTrait.underlyingCollective;

@JsonTraits(GuildMember.Trait.class)
public interface GuildMember extends User, PermissionOverridable, JsonDeserializable {
    @IntroducedBy(PRODUCTION)
    Guild getGuild();

    default Optional<String> getNickname() {
        return wrapTraitValue(Trait.NICKNAME);
    }

    default Collection<Role> getRoles() {
        return getTraitValue(Trait.ROLES);
    }

    default Instant getJoinTimestamp() {
        return getTraitValue(Trait.JOINED_TIMESTAMP);    }

    default Optional<Instant> getNitroBoostTimestamp() {
        return wrapTraitValue(Trait.NITRO_BOOST_TIMESTAMP);
    }

    default boolean isDeafened() {
        return getTraitValue(Trait.DEAFENED);
    }

    default boolean isMuted() {
        return getTraitValue(Trait.MUTED);
    }

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/guild#create-guild-ban")
    CompletableFuture<Ban> ban(int deleteMessageOfLastDays, String reason);

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/guild#remove-guild-ban")
    default CompletableFuture<Void> unban() {
        return Adapter.<Void>request(getAPI())
                .endpoint(DiscordEndpoint.BAN_SPECIFIC, getGuild().getID(), getID())
                .method(RestMethod.DELETE)
                .expectCode(HTTPStatusCodes.NO_CONTENT)
                .executeAs(data -> getAPI().getCacheManager()
                        .deleteMember(Guild.class, Ban.class, getGuild().getID(), getID()));
    }

    interface Trait extends User.Trait {
        JsonTrait<String, String> NICKNAME = identity(JsonNode::asText, "nick");
        JsonTrait<ArrayNode, Collection<Role>> ROLES = underlyingCollective("roles", Role.class, (api, data) -> api.getCacheManager()
                .getRoleByID(data.asLong())
                .orElseThrow());
        JsonTrait<String, Instant> JOINED_TIMESTAMP = simple(JsonNode::asText, "joined_at", Instant::parse);
        JsonTrait<String, Instant> NITRO_BOOST_TIMESTAMP = simple(JsonNode::asText, "premium_since", Instant::parse);
        JsonTrait<Boolean, Boolean> DEAFENED = identity(JsonNode::asBoolean, "deaf");
        JsonTrait<Boolean, Boolean> MUTED = identity(JsonNode::asBoolean, "mute");
    }

    @IntroducedBy(PRODUCTION)
    interface Updater {
        Optional<String> getNickname();

        Updater setNickname(String string);

        Collection<Role> getRoles();

        Updater addRole(Role role);

        Updater removeRoleIf(Predicate<Role> tester);

        boolean isMuted();

        Updater setMuted(boolean muted);

        boolean isDeafened();

        Updater setDeafened(boolean deafened);

        Optional<GuildVoiceChannel> getConnectedVoiceChannel();

        Updater moveToVoiceChannel(@Nullable GuildVoiceChannel guildVoiceChannel);

        default Updater kickFromVoice() {
            return moveToVoiceChannel(null);
        }

        @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/guild#modify-guild-member")
        CompletableFuture<GuildMember> update();
    }
}
