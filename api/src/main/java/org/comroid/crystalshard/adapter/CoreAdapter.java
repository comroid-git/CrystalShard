package org.comroid.crystalshard.adapter;

import com.google.common.flogger.FluentLogger;

public abstract class CoreAdapter extends Adapter {
    static {
        adapter = loadAdapter(CoreAdapter.class);
    }

    private final static   FluentLogger log = FluentLogger.forEnclosingClass();
    protected final static CoreAdapter  adapter;
}
