package de.kaleidox.crystalshard.abstraction;

import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.model.ApiBound;

public abstract class AbstractApiBound implements ApiBound {
    protected final Discord api;

    protected AbstractApiBound(Discord api) {
        this.api = api;
    }

    @Override
    public Discord getAPI() {
        return api;
    }
}
