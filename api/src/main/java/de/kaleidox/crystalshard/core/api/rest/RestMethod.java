package de.kaleidox.crystalshard.core.api.rest;

public enum RestMethod {
    GET,

    PUT,

    POST,

    PATCH,

    DELETE;

    @Override
    public String toString() {
        return name();
    }
}
