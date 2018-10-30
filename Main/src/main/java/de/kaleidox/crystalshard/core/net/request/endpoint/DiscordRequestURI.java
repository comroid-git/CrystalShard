package de.kaleidox.crystalshard.core.net.request.endpoint;

import de.kaleidox.crystalshard.main.CrystalShard;
import de.kaleidox.crystalshard.main.items.DiscordItem;
import de.kaleidox.crystalshard.util.helpers.StringHelper;
import java.net.URISyntaxException;
import java.util.Objects;

/**
 * This enum contains all endpoints which we may use.
 */
public class DiscordRequestURI extends RequestURI {
    private final static String BASE_URL = "https://discordapp.com/api/v" + CrystalShard.API_VERSION;
    private final DiscordEndpoint endpoint;

    private DiscordRequestURI(DiscordEndpoint endpoint, Object[] params) throws IllegalArgumentException, URISyntaxException {
        super(BASE_URL, endpoint.getAppendix(), params);
        this.endpoint = endpoint;
    }

    public DiscordEndpoint getEndpoint() {
        return endpoint;
    }

    public boolean sameRatelimit(Object obj) {
        if (obj instanceof DiscordRequestURI) {
            DiscordRequestURI target = (DiscordRequestURI) obj;
            if (parameters.length > 0 && Objects.nonNull(parameters[0])) return target.parameters[0].equals(parameters[0]);
            else return target.endpoint == this.endpoint;
        }
        return false;
    }

    public static DiscordRequestURI create(DiscordEndpoint endpoint, Object[] params) throws IllegalArgumentException {
        try {
            String[] passParam = new String[params.length];

            for (int i = 0; i < params.length; i++) {
                Object x = params[i];

                if (x instanceof DiscordItem) {
                    params[i] = Long.toUnsignedString(((DiscordItem) x).getId());
                } else if (x instanceof Long) {
                    params[i] = Long.toUnsignedString((Long) x);
                } else {
                    params[i] = x.toString();
                }
            }

            return new DiscordRequestURI(endpoint, passParam);
        } catch (URISyntaxException e) {
            throw new AssertionError(e);
        }
    }
}
