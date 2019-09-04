package de.kaleidox.crystalshard.api.entity.user;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

import de.kaleidox.crystalshard.adapter.Adapter;
import de.kaleidox.crystalshard.api.entity.channel.GuildVoiceChannel;
import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.api.entity.guild.Role;
import de.kaleidox.crystalshard.api.model.guild.ban.Ban;
import de.kaleidox.crystalshard.api.model.permission.PermissionOverridable;
import de.kaleidox.crystalshard.core.api.cache.CacheManager;
import de.kaleidox.crystalshard.core.api.rest.DiscordEndpoint;
import de.kaleidox.crystalshard.core.api.rest.HTTPCodes;
import de.kaleidox.crystalshard.core.api.rest.RestMethod;
import de.kaleidox.crystalshard.util.annotation.IntroducedBy;

import org.jetbrains.annotations.Nullable;

import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;
import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.PRODUCTION;

public interface GuildMember extends User, PermissionOverridable {
    @IntroducedBy(PRODUCTION)
    Guild getGuild();

    Optional<String> getNickname();

    Collection<Role> getRoles();

    Instant getJoinTimestamp();

    Optional<Instant> getNitroBoostTimestamp();

    boolean isDeafened();

    boolean isMuted();

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/guild#create-guild-ban")
    CompletableFuture<Ban> ban(int deleteMessageOfLastDays, String reason);

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/guild#remove-guild-ban")
    default CompletableFuture<Void> unban() {
        return Adapter.<Void>request(getAPI())
                .endpoint(DiscordEndpoint.BAN_SPECIFIC, getGuild().getID(), getID())
                .method(RestMethod.DELETE)
                .expectCode(HTTPCodes.EMPTY_RESPONSE)
                .executeAs(data -> CacheManager.deleteMember(Guild.class, Ban.class, getGuild().getID(), getID()));
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
