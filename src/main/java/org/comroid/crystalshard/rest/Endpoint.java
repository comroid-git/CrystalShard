package org.comroid.crystalshard.rest;

import org.comroid.crystalshard.DiscordAPI;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.entity.webhook.Webhook;
import org.comroid.restless.endpoint.AccessibleEndpoint;
import org.comroid.restless.endpoint.CompleteEndpoint;
import org.comroid.restless.endpoint.QueryParameter;
import org.intellij.lang.annotations.Language;

import java.util.regex.Pattern;

public enum Endpoint implements AccessibleEndpoint {
    GATEWAY_BOT("/gateway/bot"),

    PRIVATE_CHANNELS("/users/@me/channels"),

    SEND_MESSAGE("/channels/%s/messages", Snowflake.ID_REGEX),

    VOICE_REGIONS("/voice/regions"),

    EXECUTE_WEBHOOK("/webhooks/%s/%s%s", Webhook.ID_REGEX, Webhook.TOKEN_REGEX, QueryParameter.regex("(true)|(false)"));

    private final String extension;
    @Language("RegExp")
    private final String[] regexps;
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
        return regexps;
    }

    @Override
    public Pattern getPattern() {
        return pattern;
    }

    Endpoint(String extension, @Language("RegExp") String... regexps) {
        this.extension = extension;
        this.regexps = regexps;
        this.pattern = buildUrlPattern();
    }
}
