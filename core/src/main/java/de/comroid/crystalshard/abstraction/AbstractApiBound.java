package de.comroid.crystalshard.abstraction;

import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.api.model.ApiBound;

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
