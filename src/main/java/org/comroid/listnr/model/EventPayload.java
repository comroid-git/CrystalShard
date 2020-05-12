package org.comroid.listnr.model;

import org.comroid.spellbind.model.TypeFragment;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.container.DataContainer;
import org.comroid.varbind.container.DataContainerBase;
import org.jetbrains.annotations.Nullable;

public interface EventPayload<D, ET extends EventType<?, D, ? extends EventPayload<D, ? super ET>>>
        extends DataContainer<D>, TypeFragment {
    ET getMasterEventType();

    class Basic<D, ET extends EventType<?, D, ? extends EventPayload<D, ? super ET>>>
            extends DataContainerBase<D>
            implements EventPayload<D, ET> {
        private final ET masterEventType;

        @Override
        public ET getMasterEventType() {
            return masterEventType;
        }

        public Basic(ET masterEventType, @Nullable UniObjectNode data, @Nullable D dependent) {
            super(data, dependent);

            this.masterEventType = masterEventType;
        }
    }
}
