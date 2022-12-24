package org.comroid.crystalshard.model.gateway;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.net.URL;

public class GatewayBotResponse {
    @Getter
    @JsonProperty
    private URL url;
    @Getter
    @JsonProperty
    private int shards;
    @Getter
    @JsonProperty
    private SessionStartLimit sessionStartLimit;
}
