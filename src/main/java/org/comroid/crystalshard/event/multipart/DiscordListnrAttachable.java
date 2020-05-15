package org.comroid.crystalshard.event.multipart;

import org.comroid.crystalshard.DiscordBot;
import org.comroid.listnr.Listnr;
import org.comroid.uniform.node.UniObjectNode;

public interface DiscordListnrAttachable<
        T extends DiscordEventType<T, P>,
        P extends DiscordEventPayload<T, P>>
        extends Listnr.Attachable<UniObjectNode, DiscordBot, T, P> {
}
