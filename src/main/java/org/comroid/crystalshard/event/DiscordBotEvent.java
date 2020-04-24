package org.comroid.crystalshard.event;

import org.comroid.crystalshard.CrystalShard;
import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.model.BotBound;
import org.comroid.listnr.Event;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.VarCarrier;
import org.comroid.varbind.VariableCarrier;

public interface DiscordBotEvent<S extends DiscordBotEvent<S>> extends BotBound, Event<S>, VarCarrier.Underlying<DiscordBot> {
    abstract class Abstract<S extends DiscordBotEvent<S>> extends Event.Support.Abstract<S> implements DiscordBotEvent<S> {
        private final DiscordBot bot;
        private final VariableCarrier<DiscordBot> underlyingVarCarrier;

        protected Abstract(DiscordBot bot, UniObjectNode initialData) {
            this.bot = bot;
            this.underlyingVarCarrier = new VariableCarrier<>(CrystalShard.SERIALIZATION_ADAPTER, initialData, bot);
        }

        @Override
        public final DiscordBot getBot() {
            return bot;
        }

        @Override
        public final VarCarrier<DiscordBot> getUnderlyingVarCarrier() {
            return underlyingVarCarrier;
        }
    }
}
