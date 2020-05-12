package org.comroid.crystalshard.core.event;

import java.util.function.Function;
import java.util.function.Predicate;

import org.comroid.common.func.ParamFactory;
import org.comroid.crystalshard.CrystalShard;
import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.model.ShardBound;
import org.comroid.listnr.EventHub;
import org.comroid.listnr.model.EventType;
import org.comroid.uniform.node.UniObjectNode;

public interface GatewayEventType extends EventType<String, UniObjectNode, GatewayEvent>, ShardBound {
    final class Container {
        public final GatewayEventType TYPE_BASE;

        private final DiscordBot.Shard shard;

        public Container(DiscordBot.Shard shard) {
            this.shard = shard;

            this.TYPE_BASE = new GatewayEventType.Abstract(shard, shard.getWebSocket().getEventHub(),
                    DiscordBotEvent.class,
                    discordBotVarCarrier -> false,
                    null
            ) {};
        }
    }

    abstract class Abstract
            extends EventType.Support.Basic<GatewayEvent, UniObjectNode, String> implements GatewayEventType {
        private final DiscordBot.Shard shard;

        protected Abstract(
                DiscordBot.Shard shard, EventHub<String, UniObjectNode, ?, GatewayEvent> eventHub,
                Class<GatewayEvent> payloadType,
                Predicate<UniObjectNode> eventTester,
                ParamFactory<UniObjectNode, GatewayEvent> payloadFactory
        ) {
            super(eventHub, payloadType, eventTester,
                    ((Function<String, UniObjectNode>) str -> CrystalShard.SERIALIZATION_ADAPTER.createUniNode(str).asObjectNode()).andThen(payloadFactory));

            this.shard = shard;
        }

        @Override
        public DiscordBot.Shard getShard() {
            return shard;
        }
    }
}
