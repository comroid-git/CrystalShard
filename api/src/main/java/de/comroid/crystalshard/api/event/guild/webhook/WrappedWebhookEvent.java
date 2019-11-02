package de.comroid.crystalshard.api.event.guild.webhook;

import java.util.Optional;

import de.comroid.crystalshard.api.entity.guild.webhook.Webhook;
import org.jetbrains.annotations.Nullable;

public interface WrappedWebhookEvent extends WebhookEvent {
    @Override
    default @Nullable Webhook getTriggeringWebhook() {
        return wrapTriggeringWebhook().orElse(null);
    }

    Optional<Webhook> wrapTriggeringWebhook();
}
