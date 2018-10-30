package de.kaleidox.crystalshard.core.net.request.endpoint;

import java.net.URI;
import java.net.URISyntaxException;

public class RequestURI {
    protected final String base;
    protected final String appendix;
    protected final Object[] parameters;
    protected final URI uri;

    protected RequestURI(String base, String appendix, Object... parameters) throws IllegalArgumentException, URISyntaxException {
        this.base = base;
        this.appendix = appendix;
        this.parameters = parameters;

        int count = getParameterCount();
        if (count != parameters.length)
            throw new IllegalArgumentException((count > parameters.length ? "Not enough" : "Too many") + " parameters given!");
        else {
            uri = new URI(String.format(String.join("", base, appendix), parameters));
        }
    }

    public static RequestURI create(String base, String appendix, Object... parameters) throws IllegalArgumentException {
        try {
            return new RequestURI(base, appendix, parameters);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public String toString() {
        return uri.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RequestURI)
            return ((RequestURI) obj).uri.equals(uri);
        return false;
    }

    public String getBase() {
        return base;
    }

    public String getAppendix() {
        return appendix;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public URI getURI() {
        return uri;
    }

    protected int getParameterCount() {
        int splitted = appendix.split("%s").length - 1;
        int end = (appendix.substring(appendix.length() - 2)
                .equalsIgnoreCase("%s") ? 1 : 0);
        return splitted + end;
    }
}
