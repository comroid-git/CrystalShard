package org.comroid.crystalshard.event.multipart;

import org.comroid.common.func.Invocable;
import org.comroid.crystalshard.DiscordAPI;
import org.comroid.crystalshard.DiscordBot;
import org.comroid.listnr.model.EventContainer;
import org.comroid.uniform.node.UniObjectNode;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface DiscordEventContainer<P extends DiscordEventPayload<DiscordEventType<P>>>
        extends EventContainer<UniObjectNode, DiscordBot, DiscordEventType<P>, P> {
    Optional<DiscordAPI.Intent> getIntent();

    @Override
    DiscordEventType<P> getType();

    default DiscordEventType<P> getBaseType() {
        return getType();
    }

    final class Impl<P extends DiscordEventPayload<DiscordEventType<P>>>
            implements DiscordEventContainer<P> {
        private final @Nullable DiscordAPI.Intent intent;
        private final DiscordEventType<P> type;

        @Override
        public Optional<DiscordAPI.Intent> getIntent() {
            return Optional.ofNullable(intent);
        }

        @Override
        public DiscordEventType<P> getType() {
            return type;
        }

        public Impl(DiscordBot bot,
            Class<P> payloadType,
            Invocable.TypeMap<? extends P> payloadGenerator) {
            this(bot, null, payloadType, payloadGenerator);
        }

        public Impl(DiscordBot bot,
                    @Nullable DiscordAPI.Intent intent,
                    Class<P> payloadType,
                    Invocable.TypeMap<? extends P> payloadGenerator) {
            this.intent = intent;
            this.type = new DiscordEventType.Base<>(bot, payloadType, payloadGenerator);
        }
    }
}
