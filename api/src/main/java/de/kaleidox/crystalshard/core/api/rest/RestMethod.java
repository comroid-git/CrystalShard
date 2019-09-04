package de.kaleidox.crystalshard.core.api.rest;

public enum RestMethod {
    GET,

    PUT,

    POST,

    PATCH,

    DELETE;

    public String identifier() {
        return name();
    }
}
