package de.comroid.crystalshard.adapter;

import com.google.common.flogger.FluentLogger;
import org.jetbrains.annotations.ApiStatus.Internal;

public abstract class ImplAdapter extends Adapter {
    private final static FluentLogger log = FluentLogger.forEnclosingClass();
    protected static ImplAdapter adapter;

    @Internal
    public static void load() {
        if (adapter != null)
            throw new IllegalStateException("Already Initialized!");

        adapter = loadAdapter(ImplAdapter.class);
    }
}
