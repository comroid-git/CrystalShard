package de.comroid.crystalshard.core.gateway.listener.webhook;

import de.comroid.crystalshard.core.gateway.event.WebhooksUpdateEvent;
import de.comroid.crystalshard.core.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.gateway.listener.GatewayListenerManager;

public interface WebhooksUpdateListener extends GatewayListener<WebhooksUpdateEvent> {
    interface Manager extends GatewayListenerManager<WebhooksUpdateListener> {
    }
}
