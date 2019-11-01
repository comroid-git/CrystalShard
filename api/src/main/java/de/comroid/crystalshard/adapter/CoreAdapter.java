package de.comroid.crystalshard.adapter;

import com.google.common.flogger.FluentLogger;

public abstract class CoreAdapter extends Adapter {
    protected final static CoreAdapter adapter;
    private final static FluentLogger log;

    static {
        log = FluentLogger.forEnclosingClass();
        adapter = loadAdapter(CoreAdapter.class);
    }
}
