package org.comroid.crystalshard.auto;

import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.DiscordBotConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class AutoConfigureDiscordBot {
    @Bean(name = "discordBot")
    public DiscordBot buildDiscordBot(@Autowired DiscordBotConfig config) {
        return new DiscordBot(config);
    }
}
