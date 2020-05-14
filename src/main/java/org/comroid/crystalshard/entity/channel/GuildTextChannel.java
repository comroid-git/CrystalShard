package org.comroid.crystalshard.entity.channel;

import org.comroid.varbind.bind.GroupBind;

public interface GuildTextChannel extends GuildChannel, TextChannel {
    interface Bind extends GuildChannel.Bind, TextChannel.Bind {
        GroupBind Root = GroupBind.combine("guild_text_channel", GuildChannel.Bind.Root, TextChannel.Bind.Root);
    }
}
