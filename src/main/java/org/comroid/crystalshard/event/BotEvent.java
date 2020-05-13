package org.comroid.crystalshard.event;

import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.event.multipart.DiscordEventContainer;
import org.comroid.crystalshard.event.multipart.DiscordEventPayload;
import org.comroid.crystalshard.event.multipart.DiscordEventType;
import org.comroid.listnr.model.EventType;
import org.comroid.uniform.node.UniObjectNode;

import java.lang.reflect.Method;

/**
 * Any Discord Bot related Event
 */
public interface BotEvent {
    static Container container(DiscordBot socket) {
        return new Container(socket);
    }

    interface Payload extends DiscordEventPayload<DiscordEventType<Payload>> {
        DiscordBot getDiscordBot();
    }

    final class Container implements DiscordEventContainer<Payload> {
        public final DiscordEventType<Payload> Type;

        @Override
        public DiscordEventType<Payload> getType() {
            return Type;
        }

        public Container(DiscordBot bot) {
            this.Type = new DiscordEventType.Base<>(bot, Payload.class, this);
        }

        public Payload makePayload(DiscordBot bot, UniObjectNode data) {
        }
    }
}
