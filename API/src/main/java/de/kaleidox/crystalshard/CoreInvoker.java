package de.kaleidox.crystalshard;

import java.util.ServiceLoader;

import de.kaleidox.crystalshard.api.Discord;

import org.intellij.lang.annotations.MagicConstant;

public abstract class CoreInvoker {
    public static final CoreInvoker INSTANCE;

    static {
        INSTANCE = ServiceLoader.load(CoreInvoker.class).iterator().next();
    }

    public abstract <T> T fromIDs(
            Discord discord,
            @MagicConstant(flagsFromClass = EntityType.class) int entityType,
            long... ids
    );

    public class EntityType {
        public static final int EMOJI = 0;
        public static final int MESSAGE = 1;
        public static final int CHANNEL = 2;
        public static final int USER = 3;
        public static final int ROLE = 4;
        public static final int SERVER = 5;
    }
}
