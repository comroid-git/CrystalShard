package org.comroid.crystalshard.event.multipart;

import org.comroid.common.ref.Specifiable;
import org.comroid.crystalshard.DiscordBot;
import org.comroid.listnr.model.EventPayload;
import org.comroid.uniform.node.UniObjectNode;
import org.jetbrains.annotations.Nullable;

public interface DiscordEventPayload<
        T extends DiscordEventType<T, P>,
        P extends DiscordEventPayload<T, P>>
        extends EventPayload<DiscordBot, T, P>, Specifiable<DiscordEventPayload<T, P>> {
    final class Impl<
            T extends DiscordEventType<T, P>,
            P extends DiscordEventPayload<T, P>>
            extends EventPayload.Basic<DiscordBot, T, P>
            implements DiscordEventPayload<T, P> {
        public Impl(T masterEventType) {
            super(masterEventType);
        }
    }
}
