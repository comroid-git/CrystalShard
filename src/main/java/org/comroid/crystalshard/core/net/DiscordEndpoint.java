package org.comroid.crystalshard.core.net;

import java.net.URL;

import org.comroid.common.Polyfill;
import org.comroid.common.func.Provider;
import org.comroid.crystalshard.DiscordAPI;

public enum DiscordEndpoint {
    GATEWAY("/gateway");

    private final String extRaw;
    private final int requiredParameters;

    DiscordEndpoint(String extRaw) {
        this.extRaw = extRaw;
        this.requiredParameters = extRaw.split("%s").length - 1;
    }

    public int getRequiredParameters() {
        return requiredParameters;
    }

    public static final class EndpointProvider implements Provider.Now<URL> {
        private final DiscordEndpoint endpoint;
        private final String extRaw;
        private final Object[] args;

        private EndpointProvider(DiscordEndpoint endpoint, String extRaw, Object[] args) {
            if (endpoint.requiredParameters != args.length)
                throw new IllegalArgumentException(String.format("Insufficient argument count [expected=%d,actual=%d]",
                        endpoint.requiredParameters, args.length));
            
            this.endpoint = endpoint;
            this.extRaw   = extRaw;
            this.args     = args;
        }

        public DiscordEndpoint getEndpoint() {
            return endpoint;
        }

        public String getExtRaw() {
            return extRaw;
        }

        public Object[] getArgs() {
            return args;
        }

        public String getUnformattedUrl() {
            return DiscordAPI.URL_BASE + extRaw;
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
