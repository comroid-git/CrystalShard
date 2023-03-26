package org.comroid.crystalshard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "discord.bot")
public class DiscordBotConfig {
    public String token;
    @Builder.Default
    public int ShardId = 0;
    @Builder.Default
    public int ShardCount = 1;
}
