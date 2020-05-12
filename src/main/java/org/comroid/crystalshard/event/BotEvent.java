package org.comroid.crystalshard.event;

import org.comroid.common.func.Invocable;
import org.comroid.common.func.Provider;
import org.comroid.common.ref.StaticCache;
import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.model.BotBound;
import org.comroid.listnr.model.EventContainer;
import org.comroid.listnr.model.EventPayload;
import org.comroid.listnr.model.EventType;
import org.comroid.uniform.node.UniObjectNode;

import java.util.Collections;

/**
 * Any Discord Bot related Event
 */
public interface BotEvent {
    interface Type extends EventType<UniObjectNode, DiscordBot, Payload> {
        @Override
        default boolean test(UniObjectNode uniObjectNode) {
            return true; // todo
        }
    }

    interface Payload extends EventPayload<Type>, BotBound {
    }

    final class Container implements EventContainer<UniObjectNode, DiscordBot, Type, Payload>, BotBound {
        private final DiscordBot bot;
        private final BotEvent.Type type;

        @Override
        public final DiscordBot getBot() {
            return bot;
        }

        @Override
        public Type getType() {
            return type;
        }

        public Container(DiscordBot bot) {
            this.bot = bot;
            this.type = new TypeImpl(bot);
        }

        private final class TypeImpl extends EventType.Basic<UniObjectNode, DiscordBot, Payload> implements Type {
            @Override
            public Invocable.TypeMap<? extends Payload> getInstanceSupplier() {
                return StaticCache.access(this, "instanceSupplier",
                        () -> Invocable.<Payload>ofProvider(Provider.of(() -> new PayloadImpl(TypeImpl.this))).typeMapped());
            }

            public TypeImpl(DiscordBot bot) {
                super(Collections.emptyList(), Payload.class, bot);
            }
        }

        private final class PayloadImpl extends EventPayload.Basic<Type> implements Payload {
            @Override
            public DiscordBot getBot() {
                return bot;
            }

            public PayloadImpl(TypeImpl masterEventType) {
                super(masterEventType, data, dependent);
            }
        }
    }
}
