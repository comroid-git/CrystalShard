package org.comroid.listnr;

import static org.comroid.common.Polyfill.uncheckedCast;

public interface ListnrAttachable<IN, D, ET extends EventType<IN, D, EP>, EP extends EventPayload<ET>> {
    ListnrHub<IN, D, ? super ET, ListnrAttachable<IN, D, ? extends ET, ?>> getHub();

    default <TH extends TypeHandler<? super ET, ? super EP>> ListnrManager<? super ET, TH> attachListener(TH handler) {
        return uncheckedCast(getHub().attachListener(uncheckedCast(this), uncheckedCast(handler)));
    }
}
