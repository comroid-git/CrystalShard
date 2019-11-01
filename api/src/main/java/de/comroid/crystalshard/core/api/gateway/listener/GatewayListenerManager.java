package de.comroid.crystalshard.core.api.gateway.listener;

import java.util.Collection;
import java.util.Collections;

import de.comroid.crystalshard.api.listener.model.ListenerManager;

import org.jetbrains.annotations.Contract;

public interface GatewayListenerManager<L extends GatewayListener> extends ListenerManager<L> {
    @Override
    @Contract("-> this")
    default Collection<GatewayListenerManager<? extends GatewayListener>> getGatewayListenerManagers() {
        return Collections.singletonList(this);
    }
}
