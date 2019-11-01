package de.comroid.crystalshard.core.gateway.event.guild.ban;

import de.comroid.crystalshard.abstraction.gateway.AbstractGatewayEvent;
import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.api.entity.user.User;
import de.comroid.crystalshard.core.annotation.JsonData;
import de.comroid.crystalshard.core.api.gateway.event.guild.ban.GuildBanAddEvent;
import de.comroid.crystalshard.util.annotation.IntroducedBy;

import com.fasterxml.jackson.databind.JsonNode;

import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;

@IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/topics/gateway#guild-ban-add")
public class GuildBanAddEventImpl extends AbstractGatewayEvent implements GuildBanAddEvent {
    protected @JsonData("guild_id") long guildId;
    protected @JsonData("user") User user;

    private Guild guild;

    public GuildBanAddEventImpl(Discord api, JsonNode data) {
        super(api, data);

        guild = api.getCacheManager()
                .getGuildByID(guildId)
                .orElseThrow(() -> new AssertionError("No valid Guild ID was sent with this GuildBanAddEvent!"));

        affects(guild);
        affects(user);
        user.asGuildMember(guild).ifPresent(this::affects);

        // todo BanRemoveListener extends BanAttachableListener?
    }

    @Override
    public Guild getGuild() {
        return guild;
    }

    @Override
    public User getUser() {
        return user;
    }
}
