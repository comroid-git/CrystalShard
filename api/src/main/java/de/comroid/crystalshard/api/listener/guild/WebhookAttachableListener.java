package de.comroid.crystalshard.api.listener.guild;

import de.comroid.crystalshard.api.event.guild.webhook.WebhookEvent;
import de.comroid.crystalshard.api.listener.model.AttachableListener;
import de.comroid.crystalshard.api.listener.model.Listener;

public interface WebhookAttachableListener<E extends WebhookEvent> extends Listener<E>, AttachableListener {
}
