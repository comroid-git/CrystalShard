package org.comroid.listnr;

public interface ListnrManager<ET extends EventType, TH extends TypeHandler<ET>> {
    TH getTypeHandler();

    ET getEventSuperType();
}
