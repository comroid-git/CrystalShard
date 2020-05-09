package org.comroid.listnr;

import org.jetbrains.annotations.ApiStatus.Internal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ListnrHub<IN, D, ET extends EventType<IN, D, ?>, LA extends ListnrAttachable<? extends IN, ? extends D, ? extends ET, ?>> {
    private final Map<Class<? extends EventPayload<? extends ET>>, ? extends TypeHandler<? extends ET, ?>> handlerCache = new ConcurrentHashMap<>();
    private final List<LA> managed = new ArrayList<>();

    @Internal
    public void addManaged(LA attachable) {
        managed.add(attachable);
    }

    @Internal
    public boolean isManaged(LA attachable) {
        return managed.contains(attachable);
    }
}
