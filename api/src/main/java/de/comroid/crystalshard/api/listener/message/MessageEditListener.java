package de.comroid.crystalshard.api.listener.message;

import de.comroid.crystalshard.adapter.Adapter;
import de.comroid.crystalshard.api.event.message.MessageEditEvent;
import de.comroid.crystalshard.api.listener.ListenerSpec;
import de.comroid.crystalshard.api.listener.model.ListenerAttachable;
import de.comroid.crystalshard.api.listener.model.ListenerManager;
import de.comroid.crystalshard.core.gateway.Gateway;
import de.comroid.crystalshard.core.gateway.event.MESSAGE_UPDATE;
import de.comroid.crystalshard.util.annotation.InitializedBy;

@InitializedBy(MessageEditListener.Initializer.class)
public interface MessageEditListener extends
        ListenerSpec.AttachableTo.Discord,
        ListenerSpec.AttachableTo.Guild,
        ListenerSpec.AttachableTo.Channel,
        ListenerSpec.AttachableTo.Role,
        ListenerSpec.AttachableTo.User,
        ListenerSpec.AttachableTo.Message {
    void onMessageEdit(MessageEditEvent event);
    
    final class Initializer implements ListenerManager.Initializer<MessageEditListener> {
        @Override
        @SuppressWarnings("RedundantTypeArguments") // idea bug
        public void initialize(Gateway gateway, final MessageEditListener listener) {
            gateway.listenInStream(MESSAGE_UPDATE.class)
                    .map(ListenerAttachable.EventPair::getEvent)
                    .map(event -> Adapter.<MessageEditEvent>require(MessageEditEvent.class, event.getAffected(), event.getMessage()))
                    .map(event -> (Runnable) () -> listener.onMessageEdit(event))
                    .forEach(gateway.getAPI().getListenerThreadPool()::submit);
        }
    }
}
