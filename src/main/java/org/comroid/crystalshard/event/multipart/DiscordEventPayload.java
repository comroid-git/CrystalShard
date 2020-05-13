package org.comroid.crystalshard.event.multipart;

import org.comroid.common.ref.Specifiable;
import org.comroid.crystalshard.DiscordBot;
import org.comroid.listnr.model.EventPayload;
import org.comroid.uniform.node.UniObjectNode;
import org.jetbrains.annotations.Nullable;

public interface DiscordEventPayload<T extends DiscordEventType<? extends DiscordEventPayload<? super T>>>
        extends EventPayload<DiscordBot, T>, Specifiable<DiscordEventPayload<T>> {
    final class Impl<T extends DiscordEventType<? extends DiscordEventPayload<? super T>>>
            extends EventPayload.Basic<DiscordBot, T>
            implements DiscordEventPayload<T> {
        public Impl(DiscordBot bot, T masterEventType, @Nullable UniObjectNode data) {
            super(masterEventType, data, bot);
        }
    }
}
