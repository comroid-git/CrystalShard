package org.comroid.crystalshard.rest;

import org.comroid.crystalshard.DiscordAPI;
import org.comroid.restless.endpoint.AccessibleEndpoint;
import org.intellij.lang.annotations.Language;

import java.util.regex.Pattern;

public enum Endpoint implements AccessibleEndpoint {
    GATEWAY_BOT("/gateway/bot");

    private final String extension;
    private final @Language("RegExp") String[] regexGroups;
    private final Pattern pattern;

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
        return regexGroups;
    }

    @Override
    public Pattern getPattern() {
        return pattern;
    }

    Endpoint(String extension, @Language("RegExp") String... regexGroups) {
        this.extension = extension;
        this.regexGroups = regexGroups;
        this.pattern = buildUrlPattern();
    }
}
