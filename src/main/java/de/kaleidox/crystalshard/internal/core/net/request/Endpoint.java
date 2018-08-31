package de.kaleidox.crystalshard.internal.core.net.request;

import de.kaleidox.crystalshard.main.items.DiscordItem;
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

    public static Endpoint of(Location location, Object... parameter) {
        return location.toEndpoint(parameter);
    }

    public enum Location {
        AUDIT_LOG("/guilds/%s/audit-logs"),
        BAN("/guilds/%s/bans"),
        CHANNEL("/channels/%s"),
        CHANNEL_INVITE("/channels/%s/invites"),
        CHANNEL_TYPING("/channels/%s/typing"),
        CHANNEL_WEBHOOK("/channels/%s/webhooks"),
        CURRENT_USER("/users/@me"),
        CUSTOM_EMOJI("/guilds/%s/emojis"),
        GATEWAY("/gateway"),
        GATEWAY_BOT("/gateway/bot"),
        INVITE("/invites/%s"),
        MESSAGE("/channels/%s/messages"),
        MESSAGES_BULK_DELETE("/channels/%s/messages/bulk-delete"),
        MESSAGE_DELETE("/channels/%s/messages"),
        OWN_NICKNAME("/guilds/%s/members/@me/nick"),
        PINS("/channels/%s/pins"),
        REACTION("/channels/%s/messages/%s/reactions", 250),
        ROLE("/guilds/%s/roles"),
        SELF_INFO("/oauth2/applications/@me"),
        SERVER("/guilds"),
        SERVER_CHANNEL("/guilds/%s/channels"),
        SERVER_INVITE("/guilds/%s/invites"),
        SERVER_MEMBER("/guilds/%s/members/%s"),
        SERVER_MEMBER_ROLE("/guilds/%s/members/%s/roles/%s"),
        SERVER_PRUNE("/guilds/%s/prune"),
        SERVER_SELF("/users/@me/guilds/%s"),
        SERVER_WEBHOOK("/guilds/%s/webhooks"),
        USER("/users/%s"),
        USER_CHANNEL("/users/@me/channels"),
        WEBHOOK("/webhooks/%s");

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

        public Endpoint toEndpoint(Object... parameter) {
            String[] params = new String[parameter.length];
            int parameterCount = getParameterCount();

            for (int i = 0; i < parameter.length; i++) {
                Object x = parameter[i];

                if (x instanceof DiscordItem) {
                    params[i] = Long.toUnsignedString(((DiscordItem) x).getId());
                } else if (x instanceof Long) {
                    params[i] = Long.toUnsignedString((Long) x);
                } else {
                    params[i] = x.toString();
                }
            }

            if (parameterCount == params.length) {
                String of = String.format(location, (Object[]) params);
                URL url = UrlHelper.require(of);
                return new Endpoint(this, url);
            } else throw new IllegalArgumentException("Too " + (parameterCount > params.length ? "few" : "many") +
                    " few parameters!");
        }
    }
}
