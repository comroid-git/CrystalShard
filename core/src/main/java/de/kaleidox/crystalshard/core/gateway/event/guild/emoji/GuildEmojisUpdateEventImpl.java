package de.kaleidox.crystalshard.core.gateway.event.guild.emoji;

import java.util.Collection;

import de.kaleidox.crystalshard.abstraction.gateway.AbstractGatewayEvent;
import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.entity.emoji.CustomEmoji;
import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.api.listener.model.ListenerAttachable;
import de.kaleidox.crystalshard.core.annotation.JsonData;
import de.kaleidox.crystalshard.core.api.gateway.event.guild.emoji.GuildEmojisUpdateEvent;
import de.kaleidox.crystalshard.util.annotation.IntroducedBy;

import com.fasterxml.jackson.databind.JsonNode;

import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;

@IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/topics/gateway#guild-emojis-update")
public class GuildEmojisUpdateEventImpl extends AbstractGatewayEvent implements GuildEmojisUpdateEvent {
    protected @JsonData("guild_id") long guildId;
    protected @JsonData(value = "emojis", type = CustomEmoji.class) Collection<CustomEmoji> emojis;

    private Guild guild;

    public GuildEmojisUpdateEventImpl(Discord api, JsonNode data) {
        super(api, data);

        guild = api.getCacheManager()
                .getGuildByID(guildId)
                .orElseThrow(() -> new AssertionError("No Guild ID was sent with this GuildEmojisUpdateEvent!"));

        affects(guild);
        // todo EmojiUpdateListener extends EmojiAttachableListener? -> emojis.forEach(this::affects);
    }

    @Override
    public Guild getGuild() {
        return guild;
    }

    @Override
    public Collection<CustomEmoji> getEmojis() {
        return emojis;
    }
}
