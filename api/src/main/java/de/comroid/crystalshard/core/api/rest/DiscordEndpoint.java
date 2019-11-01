package de.comroid.crystalshard.core.api.rest;

import java.net.URI;
import java.net.URISyntaxException;

import static de.comroid.crystalshard.CrystalShard.API_BASE_URL;

public enum DiscordEndpoint {
    AUDIT_LOG("/guilds/%s/audit-logs"),
    BAN("/guilds/%s/bans"),
    BAN_SPECIFIC("/guilds/%s/bans/%s"),
    CHANNEL("/channels/%s"),
    CHANNEL_INVITE("/channels/%s/invites"),
    CHANNEL_TYPING("/channels/%s/typing"),
    CHANNEL_WEBHOOK("/channels/%s/webhooks"),
    CURRENT_USER("/users/@me"),
    CUSTOM_EMOJI("/guilds/%s/emojis"),
    CUSTOM_EMOJI_SPECIFIC("/guilds/%s/emojis/%s"),
    GATEWAY("/gateway"),
    GATEWAY_BOT("/gateway/bot"),
    GUILDS("/guilds"),
    GUILD_CHANNEL("/guilds/%s/channels"),
    GUILD_INVITES("/guilds/%s/invites"),
    GUILD_MEMBER("/guilds/%s/members/%s"),
    GUILD_MEMBER_ROLE("/guilds/%s/members/%s/roles/%s"),
    GUILD_PRUNE("/guilds/%s/prune"),
    GUILD_SELF("/users/@me/guilds/%s"),
    GUILD_SPECIFIC("/guilds/%s"),
    GUILD_WEBHOOK("/guilds/%s/webhooks"),
    GUILD_EMBED("/guilds/%s/embed"),
    GUILD_WIDGET("/guilds/%s/widget.png"),
    INVITE("/invites/%s"),
    MESSAGE("/channels/%s/messages"),
    MESSAGES_BULK_DELETE("/channels/%s/messages/bulk-delete"),
    MESSAGE_SPECIFIC("/channels/%s/messages/%s"),
    OWN_NICKNAME("/guilds/%s/members/@me/nick"),
    PINS("/channels/%s/pins"),
    REACTION("/channels/%s/messages/%s/reactions", 250),
    REACTION_SPECIFIC("/channels/%s/messages/%s/reactions/%s"),
    REACTION_SPECIFIC_USER("/channels/%s/messages/%s/reactions/%s/%s"),
    ROLE("/guilds/%s/roles"),
    ROLE_SPECIFIC("/guilds/%s/roles/%s"),
    SELF_INFO("/oauth2/applications/@me"),
    USER("/users/%s"),
    USER_CHANNEL("/users/@me/channels"),
    WEBHOOK("/webhooks/%s"),
    INTEGRATION_SPECIFIC("/guilds/%s/integrations/%s"),
    INTEGRATION_SPECIFIC_SYNC("/guilds/%s/integrations/%s/sync");

    private final String appendix;
    private int hardcodedRatelimit;

    DiscordEndpoint(String appendix) {
        this.appendix = appendix;
        hardcodedRatelimit = -1;
    }

    DiscordEndpoint(String appendix, int hardcodedRatelimit) {
        this(appendix);
        this.hardcodedRatelimit = hardcodedRatelimit;
    }

    public URI uri(Object... args) {
        int parameterCount = getParameterCount();

        if (parameterCount != args.length)
            throw new IllegalArgumentException("Invalid argument count, [" + parameterCount + "] arguments are required.");

        try {
            return new URI(String.format(API_BASE_URL + appendix, args));
        } catch (URISyntaxException e) {
            throw new AssertionError("Invalid URI", e);
        }
    }

    public int getParameterCount() {
        return appendix.split("%s").length - 1;
    }
}
