package de.kaleidox.crystalshard.core.api.gateway.listener.webhook;

import de.kaleidox.crystalshard.core.api.gateway.event.webhook.WebhooksUpdateEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListener;

@FunctionalInterface
public interface WebhooksUpdateListener extends GatewayListener<WebhooksUpdateEvent> {
    void onWebhooksUpdate(WebhooksUpdateEvent event);
}
