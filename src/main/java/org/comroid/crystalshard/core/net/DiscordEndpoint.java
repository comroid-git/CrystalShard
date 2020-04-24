package org.comroid.crystalshard.core.net;

import java.net.URL;

import org.comroid.common.Polyfill;
import org.comroid.common.func.Provider;
import org.comroid.crystalshard.DiscordAPI;

public enum DiscordEndpoint {
    GATEWAY("/gateway"),
    GATEWAY_BOT("/gateway/bot");

    private final String extRaw;
    private final int requiredParameters;

    DiscordEndpoint(String extRaw) {
        this.extRaw = extRaw;
        this.requiredParameters = extRaw.split("%s").length - 1;
    }

    public int getRequiredParameters() {
        return requiredParameters;
    }

    public EndpointProvider make(Object... args) {
        return new EndpointProvider(this, args);
    }

    public static final class EndpointProvider implements Provider.Now<URL> {
        private final DiscordEndpoint endpoint;
        private final Object[] args;

        private EndpointProvider(DiscordEndpoint endpoint, Object[] args) {
            if (endpoint.requiredParameters != args.length)
                throw new IllegalArgumentException(String.format("Insufficient argument count [expected=%d,actual=%d]",
                        endpoint.requiredParameters, args.length));
            
            this.endpoint = endpoint;
            this.args     = args;
        }

        public DiscordEndpoint getEndpoint() {
            return endpoint;
        }

        public Object[] getArgs() {
            return args;
        }

        public String getUnformattedUrl() {
            return DiscordAPI.URL_BASE + endpoint.extRaw;
        }

        public String getFullUrl() {
            return String.format(getUnformattedUrl(), args);
        }

        @Override
        public URL now() {
            return Polyfill.url(getFullUrl(), AssertionError::new);
        }
    }
}
