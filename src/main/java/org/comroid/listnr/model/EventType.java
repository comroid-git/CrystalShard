package org.comroid.listnr.model;

import org.comroid.common.Polyfill;
import org.comroid.common.func.Invocable;
import org.comroid.common.info.Dependent;
import org.comroid.common.ref.Reference;
import org.comroid.spellbind.factory.InstanceFactory;
import org.comroid.spellbind.model.TypeFragmentProvider;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface EventType<IN, D, EP extends EventPayload<D, ? extends EventType<? super IN, ? super D, ? super EP>>>
        extends Predicate<IN>, Dependent<D>, TypeFragmentProvider<EP> {
    Collection<? extends EventType<? super IN, ? super D, ? super EP>> getParents();

    Collection<EventType<? extends IN, ? extends D, ? extends EP>> getChildren();

    Invocable.TypeMap<? extends EP> getPayloadConstructor();

    @Override
    boolean test(IN in);

    void addChild(EventType<? extends IN, ? extends D, ? extends EP> child);

    default EP makePayload(IN input) {
        return makePayload(getPayloadConstructor(), input);
    }

    default <P extends EP> P makePayload(Invocable<? extends P> constructor, IN input) {
        return constructor.autoInvoke(getDependent(), input);
    }

    abstract class Basic<IN, D, EP extends EventPayload<D, ? extends EventType<? super IN, ? super D, ? super EP>>>
            implements EventType<IN, D, EP>, Reference<EP> {
        private final Collection<? extends EventType<IN, D, ? super EP>> parents;
        private final List<EventType<? extends IN, ? extends D, ? extends EP>> children = new ArrayList<>();
        private final InstanceFactory<EP, D> payloadFactory;
        private final Class<EP> payloadType;
        private final D dependent;

        @Override
        public final Collection<? extends EventType<? super IN, ? super D, ? super EP>> getParents() {
            return parents;
        }

        @Override
        public final Collection<EventType<? extends IN, ? extends D, ? extends EP>> getChildren() {
            return children;
        }

        @Override
        public final Invocable.TypeMap<? extends EP> getPayloadConstructor() {
            return payloadFactory;
        }

        @Override
        public final D getDependent() {
            return dependent;
        }

        @Override
        public final Class<EP> getInterface() {
            return payloadType;
        }

        public Basic(Collection<? extends EventType<IN, D, ?>> parents, Class<EP> payloadType, D dependent) {
            this.parents = Polyfill.uncheckedCast(parents);
            this.payloadType = payloadType;
            this.dependent = dependent;

            this.payloadFactory = new InstanceFactory<>(
                    payloadType,
                    this,
                    dependent,
                    parents.toArray(new EventType[0])
            );
        }

        @Override
        public @Nullable EP get() {
            return getInstanceSupplier().autoInvoke(getDependent());
        }

        @Override
        public final void addChild(EventType<? extends IN, ? extends D, ? extends EP> child) {
            children.add(child);
        }

        @Override
        public boolean test(IN in) {
            return Stream.concat(Stream.of(this), getChildren().stream())
                    .allMatch(it -> it.test(Polyfill.uncheckedCast(in)));
        }
    }
}
