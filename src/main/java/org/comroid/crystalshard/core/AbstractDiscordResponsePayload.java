package org.comroid.crystalshard.core;

import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.model.BotBound;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.bind.GroupBind;

public abstract class AbstractDiscordResponsePayload extends BotBound.DataBase {
    protected static final GroupBind<AbstractDiscordResponsePayload, DiscordBot> Root
            = BaseGroup.subGroup("rest-response");

    protected AbstractDiscordResponsePayload(
            DiscordBot bot,
            UniObjectNode initialData
    ) {
        super(bot, initialData);
    }
}
