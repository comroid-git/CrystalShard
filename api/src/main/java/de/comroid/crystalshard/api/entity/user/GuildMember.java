package de.comroid.crystalshard.api.entity.user;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

import de.comroid.crystalshard.adapter.Adapter;
import de.comroid.crystalshard.adapter.MainAPI;
import de.comroid.crystalshard.api.entity.EntityType;
import de.comroid.crystalshard.api.entity.channel.GuildVoiceChannel;
import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.api.entity.guild.Role;
import de.comroid.crystalshard.api.model.guild.ban.Ban;
import de.comroid.crystalshard.api.model.permission.PermissionOverridable;
import de.comroid.crystalshard.core.rest.DiscordEndpoint;
import de.comroid.crystalshard.core.rest.HTTPStatusCodes;
import de.comroid.crystalshard.core.rest.RestMethod;
import de.comroid.crystalshard.util.annotation.IntroducedBy;
import de.comroid.crystalshard.util.model.serialization.JSONBinding;
import de.comroid.crystalshard.util.model.serialization.JSONBindingLocation;
import de.comroid.crystalshard.util.model.serialization.JsonDeserializable;

import com.alibaba.fastjson.JSONObject;
import org.jetbrains.annotations.Nullable;

import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;
import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.PRODUCTION;
import static de.comroid.crystalshard.util.model.serialization.JSONBinding.identity;
import static de.comroid.crystalshard.util.model.serialization.JSONBinding.mappingCollection;
import static de.comroid.crystalshard.util.model.serialization.JSONBinding.simple;

@MainAPI
@JSONBindingLocation(GuildMember.JSON.class)
public interface GuildMember extends User, PermissionOverridable, JsonDeserializable {
    @IntroducedBy(PRODUCTION)
    Guild getGuild();

    @Override
    default EntityType getEntityType() {
        return EntityType.GUILD_MEMBER;
    }

    default Optional<String> getNickname() {
        return wrapBindingValue(JSON.NICKNAME);
    }

    default List<Role> getRoles() {
        final ArrayList<Role> list = new ArrayList<>(getBindingValue(JSON.ROLES));
        Collections.sort(list);
        return list;
    }

    default Instant getJoinTimestamp() {
        return getBindingValue(JSON.JOINED_TIMESTAMP);    }

    default Optional<Instant> getNitroBoostTimestamp() {
        return wrapBindingValue(JSON.NITRO_BOOST_TIMESTAMP);
    }

    default boolean isDeafened() {
        return getBindingValue(JSON.DEAFENED);
    }

    default boolean isMuted() {
        return getBindingValue(JSON.MUTED);
    }

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/guild#create-guild-ban")
    CompletableFuture<Ban> ban(int deleteMessageOfLastDays, String reason);

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/guild#remove-guild-ban")
    default CompletableFuture<Void> unban() {
        return Adapter.<Void>request(getAPI())
                .endpoint(DiscordEndpoint.BAN_SPECIFIC, getGuild().getID(), getID())
                .method(RestMethod.DELETE)
                .expectCode(HTTPStatusCodes.NO_CONTENT)
                .executeAsObject(data -> getAPI().getCacheManager()
                        .deleteMember(Guild.class, Ban.class, getGuild().getID(), getID()));
    }

    interface JSON extends User.JSON {
        JSONBinding.OneStage<String> NICKNAME = identity("nick", JSONObject::getString);
        JSONBinding.TriStage<Long, Role> ROLES = mappingCollection("roles", JSONObject::getLong, (api, id) -> api.getCacheManager()
                .getRoleByID(id)
                .orElseThrow());
        JSONBinding.TwoStage<String, Instant> JOINED_TIMESTAMP = simple("joined_at", JSONObject::getString, Instant::parse);
        JSONBinding.TwoStage<String, Instant> NITRO_BOOST_TIMESTAMP = simple("premium_since", JSONObject::getString, Instant::parse);
        JSONBinding.OneStage<Boolean> DEAFENED = identity("deaf", JSONObject::getBoolean);
        JSONBinding.OneStage<Boolean> MUTED = identity("mute", JSONObject::getBoolean);
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
