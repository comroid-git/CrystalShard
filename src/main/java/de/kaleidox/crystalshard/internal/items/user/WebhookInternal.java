package de.kaleidox.crystalshard.internal.items.user;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.main.items.user.Webhook;
import de.kaleidox.logging.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

public class WebhookInternal implements Webhook {
    private final static Logger logger = new Logger(WebhookInternal.class);
    private final long id;
    private final String name;
    private final URL avatarUrl;

    public WebhookInternal(JsonNode data) {
        this.id = data.get("id").asLong();
        this.name = data.get("username").asText();
        URL tempAvatarUrl;
        try {
            tempAvatarUrl = data.has("avatar") ?
                    new URL(data.get("avatar").asText()) : null;
        } catch (MalformedURLException e) {
            logger.exception(e);
            tempAvatarUrl = null;
        }
        this.avatarUrl = tempAvatarUrl;
    }

    @Override
    public Optional<URL> getAvatarUrl() {
        return Optional.ofNullable(avatarUrl);
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }
}
