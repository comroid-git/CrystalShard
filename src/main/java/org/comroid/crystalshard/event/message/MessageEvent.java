package org.comroid.crystalshard.event.message;

import org.comroid.common.func.Invocable;
import org.comroid.common.ref.StaticCache;
import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.entity.message.Message;
import org.comroid.crystalshard.model.BotBound;
import org.comroid.listnr.model.EventContainer;
import org.comroid.listnr.model.EventPayload;
import org.comroid.listnr.model.EventType;
import org.comroid.uniform.node.UniObjectNode;

import java.util.Collections;

/**
 * Any discord message related event.
 */
public interface MessageEvent {
    interface Type extends EventType<UniObjectNode, DiscordBot, Payload> {
    }

    interface Payload extends EventPayload<Type> {
        Message getMessage();

        // todo interface methods
    }

    final class Container implements EventContainer<UniObjectNode, DiscordBot, Type, Payload>, BotBound {
        private final DiscordBot bot;
        private final Type type;

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
                        () -> Invocable.<Payload>ofMethodCall(this, "craftPayload").typeMapped());
            }

            public TypeImpl(DiscordBot bot) {
                super(Collections.emptyList(), Payload.class, bot);
            }

            public Payload craftPayload(Message message) {
                return new PayloadImpl(this, message);
            }
        }

        private final class PayloadImpl extends EventPayload.Basic<Type> implements Payload {
            private final Message message;

            @Override
            public Message getMessage() {
                return message;
            }

            public PayloadImpl(TypeImpl masterEventType, Message message) {
                super(masterEventType, data, dependent);

                this.message = message;
            }
        }
    }
}
