package org.comroid.crystalshard.event;

import java.util.function.Predicate;

import org.comroid.common.func.ParamFactory;
import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.core.event.GatewayEvent;
import org.comroid.crystalshard.model.BotBound;
import org.comroid.listnr.EventType;

public interface DiscordBotEventType extends EventType<DiscordBotEvent, GatewayEvent, DiscordBotEvent>, BotBound {
    final class Container {
        public final DiscordBotEventType TYPE_BASE;

        private final DiscordBot bot;

        public Container(DiscordBot bot) {
            this.bot = bot;

            this.TYPE_BASE = new DiscordBotEventType.Abstract<>(bot,
                    DiscordBotEvent.class,
                    discordBotVarCarrier -> false,
                    null
            ) {};
        }
    }

    abstract class Abstract<P extends DiscordBotEvent>
            extends EventType.Support.Basic<DiscordBotEvent, GatewayEvent, DiscordBotEvent> implements DiscordBotEventType {
        private final DiscordBot bot;

        protected Abstract(
                DiscordBot bot,
                Class<DiscordBotEvent> payloadType,
                Predicate<DiscordBotEvent> eventTester,
                ParamFactory<DiscordBotEvent, DiscordBotEvent> payloadFactory
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
