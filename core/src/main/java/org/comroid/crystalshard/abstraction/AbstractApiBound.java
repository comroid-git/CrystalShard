package org.comroid.crystalshard.abstraction;

import org.comroid.crystalshard.api.Discord;
import org.comroid.crystalshard.api.model.ApiBound;

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
