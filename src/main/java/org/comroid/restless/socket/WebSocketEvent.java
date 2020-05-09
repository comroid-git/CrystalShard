package org.comroid.restless.socket;

import old.Event;

public interface WebSocketEvent<S extends WebSocketEvent<? super S>> extends Event<S> {
    abstract class Abstract<S extends WebSocketEvent<? super S>> extends Event.Support.Abstract<S> implements WebSocketEvent<S> {
    }
}
