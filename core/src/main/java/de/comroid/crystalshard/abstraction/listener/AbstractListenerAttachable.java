package de.comroid.crystalshard.abstraction.listener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

import de.comroid.crystalshard.CrystalShard;
import de.comroid.crystalshard.abstraction.entity.AbstractSnowflake;
import de.comroid.crystalshard.adapter.Adapter;
import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.api.event.model.Event;
import de.comroid.crystalshard.api.listener.model.AttachableListener;
import de.comroid.crystalshard.api.listener.model.Listener;
import de.comroid.crystalshard.api.listener.model.ListenerAttachable;
import de.comroid.crystalshard.api.listener.model.ListenerManager;
import de.comroid.crystalshard.util.NStreamImpl;
import de.comroid.crystalshard.util.annotation.InitializedBy;
import de.comroid.crystalshard.util.model.NStream;

import com.alibaba.fastjson.JSONObject;

import static de.comroid.crystalshard.util.Util.hackCast;

public abstract class AbstractListenerAttachable<AL extends AttachableListener & Listener, Self extends AbstractListenerAttachable<AL, Self>>
        extends AbstractSnowflake<Self>
        implements ListenerAttachable<AL> {
    protected Collection<ListenerManager<? extends AL>> listenerManagers;

    protected AbstractListenerAttachable(Discord api, JSONObject data) {
        super(api, data);

        this.listenerManagers = new ArrayList<>();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <TL extends AL> ListenerManager<TL> attachListener(TL listener) {
        Class<ListenerManager.Initializer<TL>> initializerClass = getInitializer((Class<TL>) listener.getClass());

        ListenerManager.Initializer<TL> initializer = Adapter.require(initializerClass, api, this, listener);
        initializer.initialize(getAPI().getGateway(), listener);

        return new AbstractListenerManager<TL, Event>(api, (ListenerAttachable<TL>) this, listener.getEventClass(), listener) {};
    }

    @Override
    public <TL extends AL> boolean detachListener(TL listener) {
        return listenerManagers.removeIf(manager -> manager.getListener().equals(listener));
    }

    @Override
    public Collection<ListenerManager<? extends AL>> getAttachedListenerManagers() {
        return Collections.unmodifiableCollection(listenerManagers);
    }

    @Override
    public <FE extends Event> CompletableFuture<EventPair<FE, ListenerManager<Listener>>> listenOnceTo(Class<FE> forEvent) {
        class CompletingListener implements AttachableListener, Listener {
            private final CompletableFuture<EventPair<FE, ListenerManager<Listener>>> future;
            private final AtomicReference<ListenerManager<? extends AL>> mgrRef;

            protected CompletingListener(
                    CompletableFuture<EventPair<FE, ListenerManager<Listener>>> future,
                    AtomicReference<ListenerManager<? extends AL>> mgrRef) {
                this.future = future;
                this.mgrRef = mgrRef;
            }

            @Override
            public void onEvent(FE event) {
                future.complete(new EventPair<>(event, hackCast(mgrRef.get())));
            }
        }

        class Manager extends AbstractListenerManager<CompletingListener, FE> implements ListenerManager<CompletingListener> {
            protected Manager(CompletingListener listener) {
                super(
                        AbstractListenerAttachable.this.api,
                        hackCast(AbstractListenerAttachable.this),
                        forEvent,
                        hackCast(listener)
                );
            }
        }

        final CompletableFuture<EventPair<FE, ListenerManager<Listener>>> future = new CompletableFuture<>();
        final AtomicReference<ListenerManager<? extends AL>> mgrRef = new AtomicReference<>();

        CompletingListener listener = new CompletingListener(future, mgrRef);
        ListenerManager<CompletingListener> manager = new Manager(listener).detachAfter(1);

        mgrRef.set(hackCast(manager));
        listenerManagers.add(mgrRef.get());

        return future;
    }

    @Override
    public <FE extends Event> NStream<EventPair<FE, ListenerManager<Listener>>> listenInStream(Class<FE> forEvent) {
        class StreamingListener implements AttachableListener, Listener {
            private final NStreamImpl<EventPair<FE, ListenerManager<Listener>>> streamImpl;
            private AtomicReference<ListenerManager<? extends AL>> mgrRef;

            protected StreamingListener(
                    NStreamImpl<EventPair<FE, ListenerManager<Listener>>> streamImpl,
                    AtomicReference<ListenerManager<? extends AL>> mgrRef) {
                this.streamImpl = streamImpl;
                this.mgrRef = mgrRef;
            }

            @Override
            public void onEvent(FE event) {
                streamImpl.accept(new EventPair<>(event, hackCast(mgrRef.get())));
            }
        }

        class Manager extends AbstractListenerManager<StreamingListener, FE> implements ListenerManager<StreamingListener> {
            protected Manager(StreamingListener listener) {
                super(
                        AbstractListenerAttachable.this.api,
                        hackCast(AbstractListenerAttachable.this),
                        forEvent,
                        hackCast(listener)
                );
            }
        }

        final NStreamImpl<EventPair<FE, ListenerManager<Listener>>> streamImpl = new NStreamImpl<>();
        final AtomicReference<ListenerManager<? extends AL>> mgrRef = new AtomicReference<>();

        StreamingListener listener = new StreamingListener(streamImpl, mgrRef);
        Manager manager = new Manager(listener);

        mgrRef.set(hackCast(manager));
        listenerManagers.add(mgrRef.get());

        return streamImpl;
    }

    @SuppressWarnings("unchecked")
    private <TL extends AL> Class<ListenerManager.Initializer<TL>> getInitializer(Class<TL> aClass) {
        InitializedBy initializedBy = aClass.getAnnotation(InitializedBy.class);

        Objects.requireNonNull(initializedBy, "Internal Error: Listener class " + aClass + " does not have a " +
                "@ManagedBy definition. Please open an issue at " + CrystalShard.ISSUES_URL);

        return (Class<ListenerManager.Initializer<TL>>) initializedBy.value();
    }
}
