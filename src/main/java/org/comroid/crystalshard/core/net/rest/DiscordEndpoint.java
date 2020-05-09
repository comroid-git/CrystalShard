package org.comroid.crystalshard.core.net.rest;

import org.comroid.crystalshard.DiscordAPI;
import org.comroid.restless.endpoint.RestEndpoint;

public enum DiscordEndpoint implements RestEndpoint {
    GATEWAY("/gateway"),
    GATEWAY_BOT("/gateway/bot");

    private final String extension;

    @Override
    public String getUrlBase() {
        return DiscordAPI.URL_BASE;
    }

    @Override
    public String getUrlExtension() {
        return extension;
    }

    DiscordEndpoint(String extension) {
        this.extension = extension;
    }
}
