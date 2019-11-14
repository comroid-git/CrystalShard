package de.comroid.crystalshard.api.listener.message;

import de.comroid.crystalshard.adapter.Adapter;
import de.comroid.crystalshard.api.event.message.MessageSentEvent;
import de.comroid.crystalshard.api.listener.ListenerSpec;
import de.comroid.crystalshard.api.listener.model.ListenerAttachable;
import de.comroid.crystalshard.api.listener.model.ListenerManager;
import de.comroid.crystalshard.core.gateway.Gateway;
import de.comroid.crystalshard.core.gateway.event.MESSAGE_CREATE;
import de.comroid.crystalshard.util.annotation.InitializedBy;

@InitializedBy(MessageSentListener.Initializer.class)
public interface MessageSentListener extends
        ListenerSpec.AttachableTo.Discord,
        ListenerSpec.AttachableTo.Guild,
        ListenerSpec.AttachableTo.Channel,
        ListenerSpec.AttachableTo.Role,
        ListenerSpec.AttachableTo.User {
    void onMessageSent(MessageSentEvent event);
    
    final class Initializer implements ListenerManager.Initializer<MessageSentListener> {
        @Override
        @SuppressWarnings("RedundantTypeArguments") // idea bug
        public void initialize(Gateway gateway, final MessageSentListener listener) {
            gateway.listenInStream(MESSAGE_CREATE.class)
                    .map(ListenerAttachable.EventPair::getEvent)
                    .map(event -> Adapter.<MessageSentEvent>require(MessageSentEvent.class, event.getAffected(), event.getMessage()))
                    .map(event -> (Runnable) () -> listener.onMessageSent(event))
                    .forEach(gateway.getAPI().getListenerThreadPool()::submit);
        }
    }
}
