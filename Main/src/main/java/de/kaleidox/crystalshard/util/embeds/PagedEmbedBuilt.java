package de.kaleidox.crystalshard.util.embeds;

import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.util.tunnel.TunnelAcceptor;

public interface PagedEmbedBuilt {
    interface Tunnel extends TunnelAcceptor<PagedEmbedBuilt, Message> {
    }
}
