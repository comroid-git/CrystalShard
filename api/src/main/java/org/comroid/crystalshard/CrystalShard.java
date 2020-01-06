package org.comroid.crystalshard;

import java.util.logging.Level;

import com.google.common.flogger.LoggerConfig;

public final class CrystalShard {
    public static final String VERSION = "2.0.0";

    public static final int API_VERSION = 6;

    public static final String API_BASE_URL = "https://discordapp.com/api/v" + API_VERSION;

    public static final String GATEWAY_DEFAULT_URL = "wss://gateway.discord.gg/";

    public static final String URL = "https://github.com/CrystalShardDiscord/CrystalShard";

    public static final String ISSUES_URL = URL + "/issues";
    
    public static final String PLEASE_REPORT = " Please open an issue including information about this crash at " + ISSUES_URL;
    
    public static final ThreadGroup THREAD_GROUP = new ThreadGroup("CrystalShard");

    public static final class LogLevel {
        public static final Level SKIPPED = new CustomLogLevel("SKIPPED", 950, Level.SEVERE.getResourceBundleName());
        
        private static class CustomLogLevel extends Level {
            private final String name;
            private final String resourceBundleName;

            private CustomLogLevel(String name, int value, String resourceBundleName) {
                super(name, value);

                this.name = name;
                this.resourceBundleName = resourceBundleName;
            }

            @Override
            public String getResourceBundleName() {
                return resourceBundleName;
            }

            @Override
            public String getName() {
                return name;
            }

            @Override
            public String getLocalizedName() {
                return name;
            }
        }
    }
}
