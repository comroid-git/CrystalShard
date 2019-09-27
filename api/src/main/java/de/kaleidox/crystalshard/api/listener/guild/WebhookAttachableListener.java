package de.kaleidox.crystalshard.api.listener.guild;

import de.kaleidox.crystalshard.api.entity.guild.webhook.Webhook;
import de.kaleidox.crystalshard.api.event.guild.webhook.WebhookEvent;
import de.kaleidox.crystalshard.api.listener.model.AttachableListener;
import de.kaleidox.crystalshard.api.listener.model.Listener;

public interface WebhookAttachableListener<E extends WebhookEvent> extends Listener<E>, AttachableListener {
}
