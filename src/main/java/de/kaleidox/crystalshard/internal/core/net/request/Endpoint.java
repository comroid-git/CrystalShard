package de.kaleidox.crystalshard.internal.core.net.request;

import de.kaleidox.crystalshard.main.CrystalShard;
import de.kaleidox.util.UrlHelper;

import java.net.URL;
import java.util.Optional;

/**
 * This enum contains all endpoints which we may use.
 */
public class Endpoint {
    private final Location location;
    private final URL url;

    private Endpoint(Location location, URL url) {
        this.location = location;
        this.url = url;
    }

    public Location getLocation() {
        return location;
    }

    public URL getUrl() {
        return url;
    }

    public static Endpoint of(Location location, String... parameter) {
        return location.toEndpoint(parameter);
    }

    public enum Location {
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
        REACTION("/channels/%s/messages/%s/reactions", 250),
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

        private final String location;
        private final int hardcodedRatelimit;

        Location(String location) {
            this(location, -1);
        }

        Location(String location, int hardcodedRatelimit) {
            this.location = location;
            this.hardcodedRatelimit = hardcodedRatelimit;
        }

        public String getLocation() {
            return location;
        }

        public Optional<Integer> getHardcodedRatelimit() {
            return Optional.ofNullable(hardcodedRatelimit == -1 ? null : hardcodedRatelimit);
        }

        public int getParameterCount() {
            return location.split("%s").length - 1;
        }

        public Endpoint toEndpoint(String... parameter) {
            if (getParameterCount() == parameter.length) {
                String of = String.format(location, (Object[]) parameter);
                URL url = UrlHelper.require(of);
                return new Endpoint(this, url);
            } else throw new IllegalArgumentException("Too many or too few parameters!");
        }
    }
}
