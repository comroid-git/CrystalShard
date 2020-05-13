package org.comroid.crystalshard.event.multipart;

import org.comroid.common.func.Invocable;
import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.model.BotBound;
import org.comroid.listnr.model.EventType;
import org.comroid.uniform.node.UniObjectNode;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;

public interface DiscordEventType<P extends DiscordEventPayload<? extends DiscordEventType<? super P>>>
        extends EventType<UniObjectNode, DiscordBot, P>, BotBound {
    final class Base<P extends DiscordEventPayload<? extends DiscordEventType<? super P>>>
            extends EventType.Basic<UniObjectNode, DiscordBot, P>
            implements DiscordEventType<P> {
        private final Invocable.TypeMap<? extends P> instanceSupplier;

        @Override
        public final Invocable.TypeMap<? extends P> getInstanceSupplier() {
            return instanceSupplier;
        }

        @Override
        public final DiscordBot getBot() {
            return getDependent();
        }

        public Base(DiscordBot bot, Class<P> payloadType, Method payloadSubCreatorMethod) {
            this(bot, payloadType, Invocable.<P>ofMethodCall(payloadSubCreatorMethod).typeMapped());
        }

        public Base(DiscordBot bot, Class<P> payloadType, Invocable.TypeMap<? extends P> payloadGenerator) {
            super(Collections.emptyList(), payloadType, bot);

            this.instanceSupplier = payloadGenerator;
        }
    }

    final class Combined<P extends DiscordEventPayload<? extends DiscordEventType<? super P>>>
            extends EventType.Basic<UniObjectNode, DiscordBot, P>
            implements DiscordEventType<P> {
        private final Invocable.TypeMap<? extends P> instanceSupplier;

        @Override
        public final DiscordBot getBot() {
            return getDependent();
        }

        @Override
        public final Invocable.TypeMap<? extends P> getInstanceSupplier() {
            return instanceSupplier;
        }

        @SafeVarargs
        public Combined(DiscordBot bot,
                        Class<P> payloadType,
                        Method payloadSubCreatorMethod,
                        DiscordEventType<? extends P>... subTypes) {
            super(Arrays.asList(subTypes), payloadType, bot);

            this.instanceSupplier = Invocable.<P>ofMethodCall(payloadSubCreatorMethod).typeMapped();
        }
    }
}
