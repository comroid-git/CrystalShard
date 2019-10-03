package de.kaleidox.crystalshard.core.gateway.event.guild.member;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import de.kaleidox.crystalshard.abstraction.gateway.AbstractGatewayEvent;
import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.api.entity.guild.Role;
import de.kaleidox.crystalshard.api.entity.user.GuildMember;
import de.kaleidox.crystalshard.api.entity.user.User;
import de.kaleidox.crystalshard.core.annotation.JsonData;
import de.kaleidox.crystalshard.core.api.gateway.event.guild.member.GuildMemberUpdateEvent;
import de.kaleidox.crystalshard.util.annotation.IntroducedBy;

import com.fasterxml.jackson.databind.JsonNode;

import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;

@IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/topics/gateway#guild-member-update")
public class GuildMemberUpdateEventImpl extends AbstractGatewayEvent implements GuildMemberUpdateEvent {
    protected @JsonData("guild_id") long guildId;
    protected @JsonData(value = "roles", type = Role.class) Collection<Role> userRoles;
    protected @JsonData("user") User user;
    protected @JsonData("nick") String nickname;

    private Guild guild;
    private GuildMember guildMember;

    public GuildMemberUpdateEventImpl(Discord api, JsonNode data) {
        super(api, data);

        guild = api.getCacheManager()
                .getGuildByID(guildId)
                .orElseThrow(() -> new AssertionError("No Guild ID was sent with this GuildMemberUpdateEvent!"));
        guildMember = user.asGuildMember(guild)
                .orElseThrow(() -> new AssertionError("User " + user + " is not a member of " + guild + "!"));

        affects(guild);
        affects(guildMember);
        affects(user);
    }

    @Override
    public Guild getGuild() {
        return guild;
    }

    @Override
    public Collection<Role> getRole() {
        return Collections.unmodifiableCollection(userRoles);
    }

    @Override
    public GuildMember getMember() {
        return guildMember;
    }

    @Override
    public Optional<String> getNickname() {
        return Optional.ofNullable(nickname); // todo Optional really needed here?
    }
}
