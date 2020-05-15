package org.comroid.crystalshard.entity.channel;

import org.comroid.crystalshard.DiscordBot;
import org.comroid.varbind.bind.GroupBind;

public interface GuildVoiceChannel extends GuildChannel, VoiceChannel {
    interface Bind extends GuildChannel.Bind, VoiceChannel.Bind {
        @SuppressWarnings("unchecked")
        GroupBind<GuildVoiceChannel, DiscordBot> Root
                = GroupBind.combine("guild_voice_channel", GuildChannel.Bind.Root, VoiceChannel.Bind.Root);
    }
}
