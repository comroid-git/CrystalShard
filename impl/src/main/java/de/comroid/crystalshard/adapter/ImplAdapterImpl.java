package de.comroid.crystalshard.adapter;

import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.api.entity.channel.Channel;
import de.comroid.crystalshard.api.model.channel.ChannelType;
import de.comroid.crystalshard.impl.DiscordBuilderImpl;
import de.comroid.crystalshard.impl.DiscordImpl;
import de.comroid.crystalshard.impl.entity.channel.GuildTextChannelImpl;
import de.comroid.crystalshard.impl.entity.channel.GuildVoiceChannelImpl;

import com.fasterxml.jackson.databind.JsonNode;

public final class ImplAdapterImpl extends ImplAdapter {
    public ImplAdapterImpl() throws NoSuchMethodException {
        mappingTool.implement(Discord.class, DiscordImpl.class.getConstructor(String.class, int.class))
                .implement(Discord.Builder.class, DiscordBuilderImpl.class.getConstructor())
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
