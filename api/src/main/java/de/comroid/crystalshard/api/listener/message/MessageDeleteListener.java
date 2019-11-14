package de.comroid.crystalshard.api.listener.message;

import de.comroid.crystalshard.adapter.Adapter;
import de.comroid.crystalshard.api.event.message.MessageDeleteEvent;
import de.comroid.crystalshard.api.listener.ListenerSpec;
import de.comroid.crystalshard.api.listener.model.ListenerAttachable;
import de.comroid.crystalshard.api.listener.model.ListenerManager;
import de.comroid.crystalshard.core.gateway.Gateway;
import de.comroid.crystalshard.core.gateway.event.MESSAGE_UPDATE;
import de.comroid.crystalshard.util.annotation.InitializedBy;

@InitializedBy(MessageDeleteListener.Initializer.class)
public interface MessageDeleteListener extends
        ListenerSpec.AttachableTo.Discord,
        ListenerSpec.AttachableTo.Guild,
        ListenerSpec.AttachableTo.Channel,
        ListenerSpec.AttachableTo.Role,
        ListenerSpec.AttachableTo.User,
        ListenerSpec.AttachableTo.Message {
    void onMessageDelete(MessageDeleteEvent event);
    
    final class Initializer implements ListenerManager.Initializer<MessageDeleteListener> {
        @Override
        @SuppressWarnings("RedundantTypeArguments") // idea bug
        public void initialize(Gateway gateway, final MessageDeleteListener listener) {
            gateway.listenInStream(MESSAGE_UPDATE.class)
                    .map(ListenerAttachable.EventPair::getEvent)
                    .map(event -> Adapter.<MessageDeleteEvent>require(MessageDeleteEvent.class, event.getAffected(), event.getMessage()))
                    .map(event -> (Runnable) () -> listener.onMessageDelete(event))
                    .forEach(gateway.getAPI().getListenerThreadPool()::submit);
        }
    }
}
