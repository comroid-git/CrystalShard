package org.comroid.crystalshard;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationPropertiesScan(basePackages = "org.comroid.crystalshard")
@ImportResource("classpath:beans.xml")
public class DiscordBot {
}
