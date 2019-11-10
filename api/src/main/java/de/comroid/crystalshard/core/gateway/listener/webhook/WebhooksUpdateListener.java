package de.comroid.crystalshard.core.gateway.listener.webhook;

import de.comroid.crystalshard.core.gateway.event.WEBHOOKS_UPDATE;
import de.comroid.crystalshard.core.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.gateway.listener.GatewayListenerManager;

public interface WebhooksUpdateListener extends GatewayListener<WEBHOOKS_UPDATE> {
    interface Manager extends GatewayListenerManager<WebhooksUpdateListener> {
    }
}
