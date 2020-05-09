package org.comroid.listnr;

public interface ListenerAttachable<ET extends EventType> {
    <TH extends TypeHandler<? super ET>> ListnrManager<? super ET, TH> attachListener(TH handler);
}
