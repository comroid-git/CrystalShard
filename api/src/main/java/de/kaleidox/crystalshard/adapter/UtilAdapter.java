package de.kaleidox.crystalshard.adapter;

import com.google.common.flogger.FluentLogger;

public abstract class UtilAdapter extends Adapter {
    protected final static UtilAdapter adapter;
    private final static FluentLogger log;

    static {
        log = FluentLogger.forEnclosingClass();
        adapter = loadAdapter(UtilAdapter.class);
    }
}
