package org.comroid.crystalshard;

import org.comroid.api.Polyfill;
import org.comroid.common.Version;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public final class CrystalShard {
    public static final URL URL = Polyfill.url("https://github.com/comroid-git/CrystalShard");
    public static final Version VERSION;

    static {
        try (
                InputStream is = ClassLoader.getSystemResource("crystalshard.properties").openStream()
        ) {
            Properties prop = new Properties();
            prop.load(is);

            VERSION = new Version(prop.getProperty("version"));
        } catch (IOException e) {
            throw new RuntimeException("Could not load CrystalShard", e);
        }
    }
}
