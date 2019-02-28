package de.kaleidox.crystalshard.core;

import java.util.Optional;

import de.kaleidox.crystalshard.Injector;
import de.kaleidox.crystalshard.core.net.request.DiscordRequestImpl;
import de.kaleidox.crystalshard.core.net.request.WebRequest;

public class CoreInjectionClassDefinition extends Injector {
    @Override
    protected Optional<Class> findOverride(Class forClass, Object... args) {
        Class ret = null;

        // special cases
        if (forClass == WebRequest.class && args.length > 0) return Optional.of(DiscordRequestImpl.class);

        return Optional.ofNullable(ret);
    }
}
