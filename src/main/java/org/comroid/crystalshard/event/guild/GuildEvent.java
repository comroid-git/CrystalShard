package org.comroid.crystalshard.event.guild;

import org.comroid.common.func.Invocable;
import org.comroid.common.ref.StaticCache;
import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.crystalshard.model.BotBound;
import org.comroid.listnr.EventContainer;
import org.comroid.listnr.EventPayload;
import org.comroid.listnr.EventType;
import org.comroid.uniform.node.UniObjectNode;

import java.util.Collections;

public interface GuildEvent {
    interface Type extends EventType<UniObjectNode, DiscordBot, Payload> {
    }

    interface Payload extends EventPayload<Type> {
        Guild getGuild();

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
            this.type = new Container.TypeImpl(bot);
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

            public Payload craftPayload(Guild message) {
                return new Container.PayloadImpl(this, message);
            }
        }

        private final class PayloadImpl extends EventPayload.Basic<Type> implements Payload {
            private final Guild guild;

            @Override
            public Guild getGuild() {
                return guild;
            }

            public PayloadImpl(Container.TypeImpl masterEventType, Guild guild) {
                super(masterEventType, data, dependent);

                this.guild = guild;
            }
        }
    }
}
