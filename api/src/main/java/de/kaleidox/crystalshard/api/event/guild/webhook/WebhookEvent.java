package de.kaleidox.crystalshard.api.event.guild.webhook;

import de.kaleidox.crystalshard.api.entity.guild.webhook.Webhook;
import de.kaleidox.crystalshard.api.event.model.Event;

public interface WebhookEvent extends Event {
    Webhook getWebhook();
}
