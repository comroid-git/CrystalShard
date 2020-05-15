package org.comroid.crystalshard.entity.channel;

import org.comroid.crystalshard.DiscordBot;
import org.comroid.varbind.bind.GroupBind;

public interface ChannelCategory extends GuildChannel {
    interface Bind extends GuildChannel.Bind {
        GroupBind<ChannelCategory, DiscordBot> Root = GuildChannel.Bind.Root.subGroup("channel_category");
    }
}
