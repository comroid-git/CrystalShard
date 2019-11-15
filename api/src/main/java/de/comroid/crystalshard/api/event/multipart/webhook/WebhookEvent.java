package de.comroid.crystalshard.api.event.multipart.webhook;

import de.comroid.crystalshard.api.entity.guild.webhook.Webhook;
import de.comroid.crystalshard.api.event.multipart.APIEvent;

public interface WebhookEvent extends APIEvent {
    Webhook getWebhook();
}
