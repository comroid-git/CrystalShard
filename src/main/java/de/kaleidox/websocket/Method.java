package de.kaleidox.websocket;

public enum Method {
    GET("GET"),
    PUT("PUT"),
    POST("POST"),
    PATCH("PATCH"),
    DELETE("DELETE");

    private final String descriptor;

    Method(String descriptor) {
        this.descriptor = descriptor;
    }

    public String getDescriptor() {
        return descriptor;
    }
}
