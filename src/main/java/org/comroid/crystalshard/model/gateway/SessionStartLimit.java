package org.comroid.crystalshard.model.gateway;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public class SessionStartLimit {
    @Getter
    @JsonProperty
    private int total;
    @Getter
    @JsonProperty
    private int remaining;
    @Getter
    @JsonProperty
    private int reset_after;
    @Getter
    @JsonProperty
    private int max_concurrency;
}
