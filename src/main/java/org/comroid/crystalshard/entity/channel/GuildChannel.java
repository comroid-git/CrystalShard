package org.comroid.crystalshard.entity.channel;

import org.comroid.crystalshard.entity.guild.Guild;

public interface GuildChannel extends Channel {
    Guild getGuild();
}
