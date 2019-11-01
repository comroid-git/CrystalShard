package de.comroid.crystalshard.api.event.guild.webhook;

import de.comroid.crystalshard.api.entity.guild.webhook.Webhook;
import de.comroid.crystalshard.api.event.model.Event;

public interface WebhookEvent extends Event {
    Webhook getWebhook();
}
