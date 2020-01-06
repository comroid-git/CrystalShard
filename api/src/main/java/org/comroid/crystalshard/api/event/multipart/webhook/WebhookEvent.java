package org.comroid.crystalshard.api.event.multipart.webhook;

import org.comroid.crystalshard.api.entity.guild.webhook.Webhook;
import org.comroid.crystalshard.api.event.multipart.APIEvent;

public interface WebhookEvent extends APIEvent {
    Webhook getWebhook();
}
