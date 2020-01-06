package org.comroid.crystalshard.api.event.multipart.webhook;

import java.util.Optional;

import org.comroid.crystalshard.api.entity.guild.webhook.Webhook;
import org.comroid.crystalshard.api.event.multipart.APIEvent;

import org.jetbrains.annotations.Nullable;

public interface WrappedWebhookEvent extends WebhookEvent, APIEvent {
    @Override
    default @Nullable Webhook getWebhook() {
        return wrapWebhook().orElse(null);
    }

    Optional<Webhook> wrapWebhook();
}
