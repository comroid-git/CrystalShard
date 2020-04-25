package org.comroid.crystalshard.event.guild;

import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.event.DiscordBotEvent;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.VarCarrier;

public class GuildCreated extends DiscordBotEvent.Abstract<GuildCreated> {
    public GuildCreated(VarCarrier<DiscordBot> underlyingVarCarrier) {
        super(underlyingVarCarrier);
    }
}
