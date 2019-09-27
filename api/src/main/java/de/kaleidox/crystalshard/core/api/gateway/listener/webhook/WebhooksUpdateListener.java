package de.kaleidox.crystalshard.core.api.gateway.listener.webhook;

import de.kaleidox.crystalshard.core.api.gateway.event.webhook.WebhooksUpdateEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListener;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface WebhooksUpdateListener extends GatewayListener<WebhooksUpdateEvent> {
    interface Manager extends GatewayListenerManager<WebhooksUpdateListener> {
    }
}
