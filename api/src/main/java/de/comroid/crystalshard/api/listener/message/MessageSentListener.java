package de.comroid.crystalshard.api.listener.message;

import de.comroid.crystalshard.adapter.Adapter;
import de.comroid.crystalshard.api.event.message.MessageSentEvent;
import de.comroid.crystalshard.api.listener.ListenerSpec;
import de.comroid.crystalshard.api.listener.model.ListenerAttachable;
import de.comroid.crystalshard.api.listener.model.ListenerManager;
import de.comroid.crystalshard.core.api.gateway.Gateway;
import de.comroid.crystalshard.core.api.gateway.event.message.MessageCreateEvent;
import de.comroid.crystalshard.util.annotation.InitializedBy;

@InitializedBy(MessageSentListener.Initializer.class)
public interface MessageSentListener extends
        ListenerSpec.AttachableTo.Discord<MessageSentEvent>,
        ListenerSpec.AttachableTo.Guild<MessageSentEvent>,
        ListenerSpec.AttachableTo.Channel<MessageSentEvent>,
        ListenerSpec.AttachableTo.Role<MessageSentEvent>,
        ListenerSpec.AttachableTo.User<MessageSentEvent> {
    final class Initializer implements ListenerManager.Initializer<MessageSentListener> {
        @Override
        public void initialize(Gateway gateway, final MessageSentListener listener) {
            gateway.listenInStream(MessageCreateEvent.class)
                    .map(ListenerAttachable.EventPair::getEvent)
                    .map(event -> Adapter.<MessageSentEvent>create(MessageSentEvent.class, event.getAffected(), event.getMessage()))
                    .map(event -> (Runnable) () -> listener.onEvent(event))
                    .forEach(gateway.getAPI().getListenerThreadPool()::submit);
        }
    }
}
