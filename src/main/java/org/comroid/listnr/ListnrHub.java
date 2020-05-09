package org.comroid.listnr;

import org.comroid.dreadpool.ThreadPool;
import org.jetbrains.annotations.ApiStatus.Internal;

import java.util.ArrayList;
import java.util.List;

public final class ListnrHub<IN, D, ET extends EventType<IN, D, ?>, LA extends ListnrAttachable<? extends IN, ? extends D, ? extends ET, ?>> {
    private final List<LA> managed = new ArrayList<>();
    private final ThreadPool threadPool;

    public ThreadPool getThreadPool() {
        return threadPool;
    }

    public ListnrHub(ThreadPool threadPool) {
        this.threadPool = threadPool;
    }

    @Internal
    public void addManaged(LA attachable) {
        managed.add(attachable);
    }

    @Internal
    public boolean isManaged(LA attachable) {
        return managed.contains(attachable);
    }
}
