package org.comroid.listnr;

import org.comroid.common.func.Invocable;
import org.comroid.common.info.Dependent;
import org.comroid.common.ref.Reference;
import org.comroid.spellbind.factory.InstanceFactory;
import org.comroid.spellbind.model.TypeFragmentProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public interface EventType<IN, D, EP extends EventPayload<? extends EventType<? super IN, ? super D, ? super EP>>>
        extends Predicate<IN>, Dependent<D>, TypeFragmentProvider<EP> {
    Collection<EventType<? super IN, ? super D, ? super EP>> getParents();

    Collection<EventType<? extends IN, ? extends D, ? extends EP>> getChildren();

    Invocable<? extends EP> getPayloadConstructor();

    @Override
    boolean test(IN in);

    void addChild(EventType<? extends IN, ? extends D, ? extends EP> child);

    default EP makePayload(IN input) {
        return makePayload(getPayloadConstructor(), input);
    }

    default <P extends EP> P makePayload(Invocable<? extends P> constructor, IN input) {
        return constructor.autoInvoke(getDependent(), input);
    }

    abstract class Basic<IN, D, EP extends EventPayload<? extends EventType<? super IN, ? super D, ? super EP>>>
            implements EventType<IN, D, EP> {
        private final Collection<EventType<? super IN, ? super D, ? super EP>> parents;
        private final List<EventType<? extends IN, ? extends D, ? extends EP>> children = new ArrayList<>();
        private final InstanceFactory<EP, D> payloadFactory;
        private final Reference<EP> baseFragmentSupplier;
        private final Class<EP> payloadType;
        private final D dependent;

        @Override
        public final Collection<EventType<? super IN, ? super D, ? super EP>> getParents() {
            return parents;
        }

        @Override
        public final Collection<EventType<? extends IN, ? extends D, ? extends EP>> getChildren() {
            return children;
        }

        @Override
        public final Invocable<? extends EP> getPayloadConstructor() {
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

        @Override
        public final Invocable<? extends EP> getInstanceSupplier() {
            return baseFragmentSupplier.invocable();
        }

        public Basic(Collection<EventType<? super IN, ? super D, ? super EP>> parents, Class<EP> payloadType, D dependent, Reference<EP> baseFragmentSupplier) {
            this.parents = parents;
            this.payloadType = payloadType;
            this.dependent = dependent;
            this.baseFragmentSupplier = baseFragmentSupplier;

            this.payloadFactory = new InstanceFactory<>(
                    payloadType,
                    baseFragmentSupplier,
                    dependent,
                    parents.toArray(new EventType[0])
            );
        }

        @Override
        public final void addChild(EventType<? extends IN, ? extends D, ? extends EP> child) {
            children.add(child);
        }
    }
}
