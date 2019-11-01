package de.comroid.crystalshard.core.gateway.event.webhook;

import de.comroid.crystalshard.abstraction.gateway.AbstractGatewayEvent;
import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.api.entity.channel.Channel;
import de.comroid.crystalshard.api.entity.channel.GuildTextChannel;
import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.core.annotation.JsonData;
import de.comroid.crystalshard.core.api.gateway.event.webhook.WebhooksUpdateEvent;
import de.comroid.crystalshard.util.annotation.IntroducedBy;

import com.fasterxml.jackson.databind.JsonNode;

import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;

@IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/topics/gateway#webhooks-update")
public class WebhookUpdateEventImpl extends AbstractGatewayEvent implements WebhooksUpdateEvent {
    protected @JsonData("guild_id") long guildId;
    protected @JsonData("channel_id") long channelId;

    private Guild guild;
    private GuildTextChannel channel;

    public WebhookUpdateEventImpl(Discord api, JsonNode data) {
        super(api, data);

        guild = api.getCacheManager()
                .getGuildByID(guildId)
                .orElseThrow(() -> new AssertionError("No valid Guild ID was sent with this WebhookUpdateEvent!"));
        channel = api.getCacheManager()
                .getChannelByID(channelId)
                .flatMap(Channel::asGuildTextChannel)
                .orElseThrow(() -> new AssertionError("No valid Channel ID was sent with this WebhookUpdateEvent!"));

        // todo calculate changed webhook?

        affects(guild);
        affects(channel);
    }

    @Override
    public Guild getGuild() {
        return guild;
    }

    @Override
    public GuildTextChannel getChannel() {
        return channel;
    }
}
