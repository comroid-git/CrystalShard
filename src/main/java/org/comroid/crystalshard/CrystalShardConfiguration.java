package org.comroid.crystalshard;

import lombok.Builder;
import lombok.Getter;
import org.springframework.context.annotation.Configuration;

@Builder
@Configuration
public class CrystalShardConfiguration {
    @Getter
    private String apiKey;
}
