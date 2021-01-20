package org.comroid.crystalshard.gateway.event.dispatch.webhook;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.gateway.event.GatewayEvent;
import org.comroid.crystalshard.gateway.event.dispatch.interaction.InteractionCreateEvent;
import org.comroid.uniform.node.UniNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.jetbrains.annotations.Nullable;

public final class WebhooksUpdateEvent extends GatewayEvent {
    @RootBind
    public static final GroupBind<WebhooksUpdateEvent> TYPE
            = BASETYPE.subGroup("webhooks-update", WebhooksUpdateEvent::new);

    public WebhooksUpdateEvent(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);
    }
}
