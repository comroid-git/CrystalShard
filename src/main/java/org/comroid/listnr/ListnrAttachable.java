package org.comroid.listnr;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.comroid.common.Polyfill.uncheckedCast;

public class ListnrAttachable<IN, D, ET extends EventType<IN, D, EP>, EP extends EventPayload<? super ET>> {
    private final ListnrHub<IN, D, ? super ET, ListnrAttachable<IN, D, ? extends ET, ? extends EP>> hub;
    private final Map<Class<? super EP>, List<? extends ListnrManager<? super ET, ? extends TypeHandler<? super ET, ? super EP>>>> managers
            = new ConcurrentHashMap<>();

    public final ListnrHub<IN, D, ? super ET, ListnrAttachable<IN, D, ? extends ET, ? extends EP>> getHub() {
        return hub;
    }

    public ListnrAttachable(ListnrHub<IN, D, ? super ET, ListnrAttachable<IN, D, ? extends ET, ? extends EP>> hub) {
        this.hub = hub;
    }

    public final <TH extends TypeHandler<? super ET, ? extends EventPayload<? super ET>>> ListnrManager<? super ET, TH> attachHandler(TH handler) {
        //noinspection unchecked
        final Class<? super EP> payloadType = (Class<? super EP>) handler.getEventPayloadType();
        final List<? extends ListnrManager<? super ET, ? extends TypeHandler<? super ET, ? super EP>>> payloadHandlers
                = handlerList(payloadType);

        final ListnrManager<ET, TH> manager = new ListnrManager<>(uncheckedCast(getHub()), this, handler);
        payloadHandlers.add(uncheckedCast(manager));

        return manager;
    }

    final List<? extends ListnrManager<? super ET, ? extends TypeHandler<? super ET, ? super EP>>> handlerList(
            Class<? super EP> payloadType
    ) {
        return managers.computeIfAbsent(payloadType, key -> new ArrayList<>());
    }


    public int detach(ListnrManager<ET, ? extends TypeHandler<? super ET, ? super EP>> manager) {
        return (int) managers.values()
                .stream()
                .filter(list -> list.remove(manager))
                .count();
    }
}
