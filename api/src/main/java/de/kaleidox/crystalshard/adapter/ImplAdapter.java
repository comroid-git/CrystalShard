package de.kaleidox.crystalshard.adapter;

import com.google.common.flogger.FluentLogger;

public abstract class ImplAdapter extends Adapter {
    protected final static ImplAdapter adapter;
    private final static FluentLogger log;

    static {
        log = FluentLogger.forEnclosingClass();
        adapter = loadAdapter(ImplAdapter.class);
    }
}
