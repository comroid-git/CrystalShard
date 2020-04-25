package org.comroid.crystalshard.event;

import java.util.function.Predicate;

import org.comroid.common.func.ParamFactory;
import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.model.BotBound;
import org.comroid.listnr.EventHub;
import org.comroid.listnr.EventType;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.VarCarrier;

public interface DiscordBotEventType<P extends DiscordBotEvent>
        extends EventType<P, UniObjectNode, VarCarrier<DiscordBot>>, BotBound {
    final class Container {
        public final DiscordBotEventType<DiscordBotEvent> TYPE_BASE;

        private final DiscordBot bot;
        private final EventHub<UniObjectNode, VarCarrier<DiscordBot>> hub;

        public Container(DiscordBot bot) {
            this.bot = bot;
            this.hub = bot.getEventHub();

            this.TYPE_BASE = new DiscordBotEventType.Abstract<>(bot,
                    DiscordBotEvent.class,
                    discordBotVarCarrier -> false,
                    null
            ) {};
        }
    }

    abstract class Abstract<P extends DiscordBotEvent> extends EventType.Support.Basic<P, UniObjectNode, VarCarrier<DiscordBot>>
            implements DiscordBotEventType<P> {
        private final DiscordBot bot;

        protected Abstract(
                DiscordBot bot,
                Class<P> payloadType,
                Predicate<VarCarrier<DiscordBot>> eventTester,
                ParamFactory<VarCarrier<DiscordBot>, P> payloadFactory
        ) {
            super(bot.getEventHub(), payloadType, eventTester, payloadFactory);

            this.bot = bot;
        }

        @Override
        public final DiscordBot getBot() {
            return bot;
        }
    }
}
