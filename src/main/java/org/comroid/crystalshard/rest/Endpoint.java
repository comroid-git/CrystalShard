package org.comroid.crystalshard.rest;

import org.comroid.crystalshard.DiscordAPI;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.restless.endpoint.AccessibleEndpoint;
import org.intellij.lang.annotations.Language;

import java.util.regex.Pattern;

public enum Endpoint implements AccessibleEndpoint {
    GATEWAY_BOT("/gateway/bot"),

    PRIVATE_CHANNELS("/users/@me/channels"),

    SEND_MESSAGE("/channels/%s/messages", Snowflake.ID_REGEX),

    VOICE_REGIONS("/voice/regions");

    private final String extension;
    private final @Language("RegExp")
    String[] regexps;
    private final Pattern pattern = buildUrlPattern();

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
        return regexps;
    }

    @Override
    public Pattern getPattern() {
        return pattern;
    }

    Endpoint(String extension, @Language("RegExp") String... regexps) {
        this.extension = extension;
        this.regexps = regexps;
    }
}
