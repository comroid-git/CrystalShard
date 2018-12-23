package de.kaleidox.crystalshard.util;

import java.util.Hashtable;

public class UtilInjectorExtensiveImpl extends UtilInjector {
    private final static Hashtable<Class, Class> implementations;

    static {
        implementations = new Hashtable<>();
        implementations.put(DiscordUtils.class, DiscordUtilsImpl.class);
        implementations.put(DefaultEmbed.class, DefaultEmbedImpl.class);
    }

    public UtilInjectorExtensiveImpl() {
        super(implementations);
    }

    @Override
    public int getJdkVersion() {
        return 9;
    }
}
