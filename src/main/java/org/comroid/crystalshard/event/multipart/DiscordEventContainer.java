package org.comroid.crystalshard.event.multipart;

import org.comroid.common.func.Invocable;
import org.comroid.crystalshard.DiscordAPI;
import org.comroid.crystalshard.DiscordBot;
import org.comroid.listnr.model.EventContainer;
import org.comroid.uniform.node.UniObjectNode;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface DiscordEventContainer<
        T extends DiscordEventType<T, P>,
        P extends DiscordEventPayload<T, P>>
        extends EventContainer<UniObjectNode, DiscordBot, T, P> {
    Optional<DiscordAPI.Intent> getIntent();

    @Override
    T getType();

    default DiscordEventType<? super T, ? super P> getBaseType() {
        return getType();
    }

    final class Impl<
            T extends DiscordEventType<T, P>,
            P extends DiscordEventPayload<T, P>>
            implements DiscordEventContainer<T, P> {
        private final @Nullable DiscordAPI.Intent intent;
        private final T type;

        @Override
        public Optional<DiscordAPI.Intent> getIntent() {
            return Optional.ofNullable(intent);
        }

        @Override
        public T getType() {
            return type;
        }

        public Impl(DiscordBot bot,
                    Class<P> payloadType,
                    Invocable.TypeMap<? extends P> payloadGenerator,
                    T type) {
            this(bot, null, payloadType, payloadGenerator, type);
        }

        public Impl(DiscordBot bot,
                    @Nullable DiscordAPI.Intent intent,
                    Class<P> payloadType,
                    Invocable.TypeMap<? extends P> payloadGenerator,
                    T type) {
            this.intent = intent;
            this.type = type;
        }
    }
}
