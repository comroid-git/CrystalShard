package de.comroid.crystalshard.core.gateway.event.guild.ban;

import de.comroid.crystalshard.abstraction.gateway.AbstractGatewayEvent;
import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.api.entity.user.User;
import de.comroid.crystalshard.api.listener.model.ListenerAttachable;
import de.comroid.crystalshard.core.api.gateway.event.guild.ban.GuildBanRemoveEvent;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class GuildBanRemoveEventImpl extends AbstractGatewayEvent implements GuildBanRemoveEvent {
    protected @JsonProperty("guild_id") long guildId;
    protected @JsonProperty("user") User user;

    private Guild guild;

    protected GuildBanRemoveEventImpl(Discord api, JsonNode data, ListenerAttachable[] affected) {
        super(api, data);

        guild = api.getCacheManager()
                .getGuildByID(guildId)
                .orElseThrow(() -> new AssertionError("No Guild ID was sent with this GuildBanRemoveEvent!"));
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
