package de.comroid.crystalshard.core.api.gateway.listener.webhook;

import de.comroid.crystalshard.core.api.gateway.event.WebhooksUpdateEvent;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface WebhooksUpdateListener extends GatewayListener<WebhooksUpdateEvent> {
    interface Manager extends GatewayListenerManager<WebhooksUpdateListener> {
    }
}
