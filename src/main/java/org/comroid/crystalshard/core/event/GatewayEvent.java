package org.comroid.crystalshard.core.event;

import org.comroid.common.ref.Specifiable;
import org.comroid.crystalshard.CrystalShard;
import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.model.BotBound;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.VarCarrier;
import org.comroid.varbind.VariableCarrier;

public interface GatewayEvent
        extends BotBound, WebSocketEvent<GatewayEvent>, VarCarrier.Underlying<DiscordBot>, Specifiable<GatewayEvent> {
    abstract class Abstract extends WebSocketEvent.Abstract<GatewayEvent> implements GatewayEvent {
        private final DiscordBot             bot;
        private final VarCarrier<DiscordBot> underlyingVarCarrier;

        protected Abstract(DiscordBot bot, UniObjectNode initialData) {
            this.bot                  = bot;
            this.underlyingVarCarrier = new VariableCarrier<>(CrystalShard.SERIALIZATION_ADAPTER, initialData, bot);
        }

        protected Abstract(VarCarrier<DiscordBot> underlyingVarCarrier) {
            this.bot                  = underlyingVarCarrier.getDependencyObject();
            this.underlyingVarCarrier = underlyingVarCarrier;
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
