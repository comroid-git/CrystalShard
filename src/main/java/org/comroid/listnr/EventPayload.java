package org.comroid.listnr;

import org.comroid.spellbind.model.TypeFragment;

public interface EventPayload<ET extends EventType<?, ?, ? extends EventPayload<? super ET>>> extends TypeFragment {
    ET getMasterEventType();

    class Basic<ET extends EventType<?, ?, ? extends EventPayload<? super ET>>> implements EventPayload<ET> {
        private final ET masterEventType;

        @Override
        public ET getMasterEventType() {
            return masterEventType;
        }

        protected Basic(ET masterEventType) {
            this.masterEventType = masterEventType;
        }
    }
}
