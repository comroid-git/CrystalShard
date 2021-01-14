package org.comroid.crystalshard.rest;

import org.comroid.crystalshard.DiscordAPI;
import org.comroid.restless.endpoint.AccessibleEndpoint;
import org.intellij.lang.annotations.Language;

import java.util.regex.Pattern;

public enum Endpoint implements AccessibleEndpoint {
    GATEWAY("/gateway"),
    GATEWAY_BOT("/gateway/bot"),
    MESSAGE("/channels/%s/messages"),
    MESSAGE_DELETE("/channels/%s/messages"),
    MESSAGES_BULK_DELETE("/channels/%s/messages/bulk-delete"),
    CHANNEL_TYPING("/channels/%s/typing"),
    CHANNEL_INVITE("/channels/%s/invites"),
    USER("/users/%s"),
    USER_CHANNEL("/users/@me/channels"),
    CHANNEL("/channels/%s"),
    ROLE("/guilds/%s/roles"),
    SERVER("/guilds"),
    SERVER_PRUNE("/guilds/%s/prune"),
    SERVER_SELF("/users/@me/guilds/%s"),
    SERVER_CHANNEL("/guilds/%s/channels"),
    REACTION("/channels/%s/messages/%s/reactions"),
    PINS("/channels/%s/pins"),
    SERVER_MEMBER("/guilds/%s/members/%s"),
    SERVER_MEMBER_ROLE("/guilds/%s/members/%s/roles/%s"),
    OWN_NICKNAME("/guilds/%s/members/@me/nick"),
    SELF_INFO("/oauth2/applications/@me"),
    CHANNEL_WEBHOOK("/channels/%s/webhooks"),
    SERVER_WEBHOOK("/guilds/%s/webhooks"),
    SERVER_INVITE("/guilds/%s/invites"),
    WEBHOOK("/webhooks/%s"),
    INVITE("/invites/%s"),
    BAN("/guilds/%s/bans"),
    CURRENT_USER("/users/@me"),
    AUDIT_LOG("/guilds/%s/audit-logs"),
    CUSTOM_EMOJI("/guilds/%s/emojis");

    private final String extension;
    private final @Language("RegExp") String[] regexps;
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
