package org.comroid.crystalshard;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.PostLoad;
import lombok.Getter;
import org.comroid.api.Polyfill;
import org.comroid.api.Version;
import org.comroid.crystalshard.annotation.EnableCrystalShard;
import org.comroid.crystalshard.rest.Gateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Component
@ConditionalOnClass(EnableCrystalShard.class)
public class CrystalShard {
    private static final Logger log = LoggerFactory.getLogger(CrystalShard.class);
    public static final URL URL = Polyfill.url("https://github.com/comroid-git/CrystalShard");
    public static final Version VERSION;
    public static final String toString;

    static {
        try (
                InputStream is = ClassLoader.getSystemResource("org/comroid/crystalshard/info.properties").openStream()
        ) {
            final Properties prop = new Properties();
            prop.load(is);

            VERSION = new Version(prop.getProperty("version"));
            toString = String.format("CrystalShard @ v%s (%s)", VERSION, URL);
        } catch (Throwable e) {
            throw new RuntimeException("Could not load CrystalShard properties", e);
        }
    }

    @Getter
    @Autowired
    private CrystalShardConfiguration config;
    @Autowired
    private Gateway
    private final List<DiscordBotShard> shards = new ArrayList<>();

    @PostConstruct
    private void init() {
        log.info("Starting up DiscordBot...");
    }

    @Override
    public String toString() {
        return toString;
    }
}
