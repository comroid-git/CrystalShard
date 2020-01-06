package org.comroid.crystalshard.core.gateway.event;

// https://discordapp.com/developers/docs/topics/gateway#resumed

import org.comroid.crystalshard.adapter.MainAPI;
import org.comroid.crystalshard.util.model.serialization.JSONBindingLocation;

@MainAPI
@JSONBindingLocation(RESUMED.JSON.class)
public interface RESUMED extends GatewayEventBase {
    interface JSON {}
}
