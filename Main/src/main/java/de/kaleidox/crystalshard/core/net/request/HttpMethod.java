package de.kaleidox.crystalshard.core.net.request;

public enum HttpMethod {
    GET("GET"),
    PUT("PUT"),
    POST("POST"),
    PATCH("PATCH"),
    DELETE("DELETE");
    private final String descriptor;
    
    HttpMethod(String descriptor) {
        this.descriptor = descriptor;
    }
    
    public String getDescriptor() {
        return descriptor;
    }
}
