package org.comroid.crystalshard;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;

@Component
@ConditionalOnBean(DiscordBotConfig.class)
@ConfigurationPropertiesScan(basePackages = "org.comroid.crystalshard")
@ImportResource("classpath:beans.xml")
public class DiscordBot {
    public static final String CACHE_SCHEMA = "discord_cache";
    public static final String CACHE_FILE;

    static {
        try {
            CACHE_FILE = File.createTempFile(CACHE_SCHEMA, ".db").getAbsolutePath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private final DiscordBotConfig config;

    public DiscordBot(DiscordBotConfig config) {
        this.config = config;
    }

    @Bean
    @Order
    public DataSource CrystalShardCache() {
        return DataSourceBuilder.create()
                .url("jdbc:h2:file:%s;CIPHER=AES".formatted(CACHE_FILE))
                .build();
    }
}
