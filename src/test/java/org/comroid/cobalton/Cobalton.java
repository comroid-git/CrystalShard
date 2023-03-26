package org.comroid.cobalton;

import org.comroid.crystalshard.DiscordBotConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Cobalton {
    @Bean
    public DiscordBotConfig botConfig() {
        return DiscordBotConfig.builder()
                .token("token")
                .build();
    }
}
