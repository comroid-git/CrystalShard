package de.kaleidox.crystalshard.core.net.request.endpoint;

import java.util.Optional;

public enum DiscordEndpoint {
    AUDIT_LOG("/guilds/%s/audit-logs"),
    BAN("/guilds/%s/bans"),
    CHANNEL("/channels/%s"),
    CHANNEL_INVITE("/channels/%s/invites"),
    CHANNEL_TYPING("/channels/%s/typing"),
    CHANNEL_WEBHOOK("/channels/%s/webhooks"),
    CURRENT_USER("/users/@me"),
    CUSTOM_EMOJI("/guilds/%s/emojis"),
    CUSTOM_EMOJI_SPECIFIC("/guilds/%s/emoji/%s"),
    GATEWAY("/gateway"),
    GATEWAY_BOT("/gateway/bot"),
    GUILD("/guilds"),
    GUILD_CHANNEL("/guilds/%s/channels"),
    GUILD_INVITE("/guilds/%s/invites"),
    GUILD_MEMBER("/guilds/%s/members/%s"),
    GUILD_MEMBER_ROLE("/guilds/%s/members/%s/roles/%s"),
    GUILD_PRUNE("/guilds/%s/prune"),
    GUILD_SPECIFIC("/guilds/%s"),
    GUILD_WEBHOOK("/guilds/%s/webhooks"),
    GUILD_VANITY_INVITE("/guilds/%s/vanity-url"),
    INVITE("/invites/%s"),
    GUILD_INTEGRATIONS("/guilds/%s/integrations"),
    MESSAGE("/channels/%s/messages"),
    MESSAGES_BULK_DELETE("/channels/%s/messages/bulk-delete"),
    MESSAGE_DELETE("/channels/%s/messages"),
    MESSAGE_SPECIFIC("/channels/%s/messages/%s"),
    PINS("/channels/%s/pins"),
    PIN_MESSAGE("/channels/%s/pins/%s"),
    REACTIONS("/channels/%s/messages/%s/reactions", 250),
    REACTION_OWN("/channels/%s/messages/%s/reactions/%s/@me", 250),
    REACTION_USER("/channels/%s/messages/%s/reactions/%s/%s", 250),
    GUILD_ROLES("/guilds/%s/roles"),
    GUILD_ROLE_SPECIFIC("/guilds/%s/role/%s"),
    SELF_CHANNELS("/users/@me/channels"),
    SELF_GUILD("/users/@me/guilds/%s"),
    SELF_INFO("/oauth2/applications/@me"),
    SELF_NICKNAME("/guilds/%s/members/@me/nick"),
    USER("/users/%s"),
    USER_CHANNEL("/users/@me/channels"),
    WEBHOOK("/webhooks/%s");

    private final String appendix;
    private final int hardcodedRatelimit;

    DiscordEndpoint(String appendix) {
        this(appendix, -1);
    }

    DiscordEndpoint(String appendix, int hardcodedRatelimit) {
        this.appendix = appendix;
        this.hardcodedRatelimit = hardcodedRatelimit;
    }

    public String getAppendix() {
        return appendix;
    }

    public Optional<Integer> getHardcodedRatelimit() {
        return Optional.ofNullable(hardcodedRatelimit == -1 ? null : hardcodedRatelimit);
    }

    public DiscordRequestURI createUri(Object... params) throws IllegalArgumentException {
        return DiscordRequestURI.create(this, params);
    }
}
