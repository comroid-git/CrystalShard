package org.comroid.crystalshard.model.embed;

import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.model.BotBound;
import org.comroid.varbind.container.DataContainer;

public interface EmbedMember extends BotBound, DataContainer<DiscordBot> {
    Embed getEmbed();
}
