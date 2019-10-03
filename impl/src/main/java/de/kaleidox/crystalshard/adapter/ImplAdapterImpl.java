package de.kaleidox.crystalshard.adapter;

import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.entity.channel.Channel;
import de.kaleidox.crystalshard.api.model.channel.ChannelType;
import de.kaleidox.crystalshard.impl.DiscordImpl;
import de.kaleidox.crystalshard.impl.entity.channel.GuildTextChannelImpl;
import de.kaleidox.crystalshard.impl.entity.channel.GuildVoiceChannelImpl;

import com.fasterxml.jackson.databind.JsonNode;

public final class ImplAdapterImpl extends ImplAdapter {
    public ImplAdapterImpl() throws NoSuchMethodException {
        mappingTool.implement(Discord.class, DiscordImpl.class.getConstructor(String.class)) // todo
                .implement(Channel.class, ImplAdapterImpl.class.getDeclaredMethod("decideChannel", Discord.class, JsonNode.class));
    }

    public static Channel decideChannel(Discord api, JsonNode data) {
        ChannelType type = ChannelType.valueOf((String) Channel.Trait.CHANNEL_TYPE.extract(data));

        return switch (type) {
            case GUILD_TEXT -> new GuildTextChannelImpl(api, data);
            case DM -> null;
            case GUILD_VOICE -> new GuildVoiceChannelImpl(api, data);
            case GROUP_DM -> null;
            case GUILD_CATEGORY -> null;
            case GUILD_NEWS -> null;
            case GUILD_STORE -> null;
        };
    }
}
