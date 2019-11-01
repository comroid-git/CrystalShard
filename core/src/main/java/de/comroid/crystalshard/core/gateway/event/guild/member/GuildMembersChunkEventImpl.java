package de.comroid.crystalshard.core.gateway.event.guild.member;

import java.util.Collection;
import java.util.Collections;

import de.comroid.crystalshard.abstraction.gateway.AbstractGatewayEvent;
import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.api.entity.user.GuildMember;
import de.comroid.crystalshard.core.annotation.JsonData;
import de.comroid.crystalshard.core.api.gateway.event.guild.member.GuildMembersChunkEvent;
import de.comroid.crystalshard.util.annotation.IntroducedBy;

import com.fasterxml.jackson.databind.JsonNode;

import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;

@IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/topics/gateway#guild-members-chunk")
public class GuildMembersChunkEventImpl extends AbstractGatewayEvent implements GuildMembersChunkEvent {
    protected @JsonData("guild_id") long guildId;
    protected @JsonData(value = "members", type = GuildMember.class) Collection<GuildMember> guildMembers;
    protected @JsonData(value = "not_found", type = Long.class) Collection<Long> notFound;

    private Guild guild;

    public GuildMembersChunkEventImpl(Discord api, JsonNode data) {
        super(api, data);

        guild = api.getCacheManager()
                .getGuildByID(guildId)
                .orElseThrow(() -> new AssertionError("No Guild ID was sent with this GuildMembersChunkEvent!"));

        affects(guild);
        guildMembers.forEach(this::affects);
    }

    @Override
    public Guild getGuild() {
        return guild;
    }

    @Override
    public Collection<GuildMember> getMembers() {
        return Collections.unmodifiableCollection(guildMembers);
    }

    @Override
    public Collection<Long> getUnknownIDs() {
        return Collections.unmodifiableCollection(notFound);
    }
}
