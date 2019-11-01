package de.comroid.crystalshard.core.gateway.event.guild.member;

import de.comroid.crystalshard.abstraction.gateway.AbstractGatewayEvent;
import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.api.entity.user.GuildMember;
import de.comroid.crystalshard.core.annotation.JsonData;
import de.comroid.crystalshard.core.api.gateway.event.guild.member.GuildMemberAddEvent;
import de.comroid.crystalshard.util.annotation.IntroducedBy;

import com.fasterxml.jackson.databind.JsonNode;

import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;

@IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/topics/gateway#guild-member-add")
public class GuildMemberAddEventImpl extends AbstractGatewayEvent implements GuildMemberAddEvent {
    protected @JsonData GuildMember member;
    protected @JsonData("guild_id") long guildId;

    private Guild guild;

    public GuildMemberAddEventImpl(Discord api, JsonNode data) {
        super(api, data);

        guild = api.getCacheManager()
                .getGuildByID(guildId)
                .orElseThrow(() -> new AssertionError("No valid Guild ID was sent with this GuildMemberAddEvent!"));

        affects(guild);
        affects(member);
    }

    @Override
    public Guild getGuild() {
        return guild;
    }

    @Override
    public GuildMember getMember() {
        return member;
    }
}
