package de.comroid.crystalshard.api.listener.message.reaction;

import de.comroid.crystalshard.adapter.Adapter;
import de.comroid.crystalshard.api.event.message.reaction.ReactionRemoveEvent;
import de.comroid.crystalshard.api.event.model.Event;
import de.comroid.crystalshard.api.listener.ListenerSpec;
import de.comroid.crystalshard.api.listener.model.ListenerAttachable;
import de.comroid.crystalshard.api.listener.model.ListenerManager;
import de.comroid.crystalshard.core.gateway.Gateway;
import de.comroid.crystalshard.core.gateway.event.MESSAGE_UPDATE;
import de.comroid.crystalshard.util.annotation.InitializedBy;

@InitializedBy(ReactionRemoveListener.Initializer.class)
public interface ReactionRemoveListener extends
        ListenerSpec.AttachableTo.Discord,
        ListenerSpec.AttachableTo.Guild,
        ListenerSpec.AttachableTo.Channel,
        ListenerSpec.AttachableTo.Role,
        ListenerSpec.AttachableTo.User,
        ListenerSpec.AttachableTo.Message {
    void onReactionRemove(ReactionRemoveEvent event);
    
    final class Initializer implements ListenerManager.Initializer<ReactionRemoveListener> {
        @Override
        @SuppressWarnings("RedundantTypeArguments") // idea bug
        public void initialize(Gateway gateway, final ReactionRemoveListener listener) {
            gateway.listenInStream(MESSAGE_UPDATE.class)
                    .map(ListenerAttachable.EventPair::getEvent)
                    .map(event -> Adapter.<ReactionRemoveEvent>require(ReactionRemoveEvent.class, event.getAffected(), event.getMessage()))
                    .map(event -> (Runnable) () -> listener.onReactionRemove(event))
                    .forEach(gateway.getAPI().getListenerThreadPool()::submit);
        }
    }
}
