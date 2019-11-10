package de.comroid.crystalshard.core.rest;

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
