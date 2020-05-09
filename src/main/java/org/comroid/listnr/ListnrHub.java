package org.comroid.listnr;

import org.comroid.dreadpool.ThreadPool;
import org.jetbrains.annotations.ApiStatus.Internal;

import java.util.ArrayList;
import java.util.List;

public final class ListnrHub<IN, D, ET extends EventType<IN, D, ?>, LA extends ListnrAttachable<? extends IN, ? extends D, ? extends ET, ?>> {
    private final List<? extends EventType<? super IN, ? super D, ? extends EventPayload<? super ET>>> managedTypes = new ArrayList<>();
    private final List<LA> managedAttachables = new ArrayList<>();
    private final ThreadPool threadPool;

    public ThreadPool getThreadPool() {
        return threadPool;
    }

    public ListnrHub(ThreadPool threadPool) {
        this.threadPool = threadPool;
    }

    @Internal
    public void manage(EventType<? super IN, ? super D, ? extends EventPayload<? super ET>> type) {

    }

    @Internal
    public void attach(LA attachable) {
        managedAttachables.add(attachable);
    }

    @Internal
    public boolean isAttached(LA attachable) {
        return managedAttachables.contains(attachable);
    }
}
