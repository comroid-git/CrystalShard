package org.comroid.crystalshard;

import org.comroid.api.Polyfill;
import org.comroid.common.Version;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public final class CrystalShard {
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
}
