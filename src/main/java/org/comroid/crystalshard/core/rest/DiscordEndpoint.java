package org.comroid.crystalshard.core.rest;

import org.comroid.crystalshard.DiscordAPI;
import org.comroid.restless.endpoint.AccessibleEndpoint;

public enum DiscordEndpoint implements AccessibleEndpoint {
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

    @Override
    public String[] getRegExpGroups() {
        return new String[0];
    }

    DiscordEndpoint(String extension) {
        this.extension = extension;
    }
}
