package org.comroid.listnr;

public interface EventContainer<IN, D, ET extends EventType<IN, D, ? super EP>, EP extends EventPayload<? super ET>> {
    ET getType();
}
