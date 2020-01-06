package org.comroid.crystalshard.adapter;

import org.comroid.crystalshard.api.Discord;
import org.comroid.crystalshard.api.entity.channel.Channel;
import org.comroid.crystalshard.api.model.channel.ChannelType;
import org.comroid.crystalshard.impl.DiscordBuilderImpl;
import org.comroid.crystalshard.impl.DiscordImpl;

import com.alibaba.fastjson.JSONObject;

public final class ImplAdapterImpl extends ImplAdapter {
    public ImplAdapterImpl() throws NoSuchMethodException {
        mappingTool.implement(Discord.class, DiscordImpl.class.getConstructor(String.class, int.class))
                .implement(Discord.Builder.class, DiscordBuilderImpl.class.getConstructor())
                .implement(Channel.class, ImplAdapterImpl.class.getDeclaredMethod("decideChannel", Discord.class, JSONObject.class));
    }

    public static Channel decideChannel(Discord api, JSONObject data) {
        ChannelType type = ChannelType.valueOf(Channel.JSON.CHANNEL_TYPE.extractValue(data));

        return switch (type) {
            case GUILD_TEXT -> new GuildTextChannelImpl(api, data);
            case DM -> new PrivateTextChannelImpl(api, data);
            case GUILD_VOICE -> new GuildVoiceChannelImpl(api, data);
            case GROUP_DM -> new GroupTextChannelImpl(api, data);
            case GUILD_CATEGORY -> new GuildChannelCategoryImpl(api, data);
            case GUILD_NEWS -> new GuildNewsChannelImpl(api, data);
            case GUILD_STORE -> new GuildStoreChannelImpl(api, data);
        };
    }
}
