package org.comroid.listnr;

public class ListnrAttachable<IN, D, ET extends EventType<IN, D, EP>, EP extends EventPayload<ET>> {
    private final ListnrHub<IN, D, ? super ET, ListnrAttachable<IN, D, ? extends ET, ?>> hub;

    public final ListnrHub<IN, D, ? super ET, ListnrAttachable<IN, D, ? extends ET, ?>> getHub() {
        return hub;
    }

    public ListnrAttachable(ListnrHub<IN, D, ? super ET, ListnrAttachable<IN, D, ? extends ET, ?>> hub) {
        this.hub = hub;
    }

    public <TH extends TypeHandler<? super ET, ? super EP>> ListnrManager<? super ET, TH> attachListener(TH handler) {
    }
}
