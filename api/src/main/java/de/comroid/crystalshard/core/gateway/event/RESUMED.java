package de.comroid.crystalshard.core.gateway.event;

// https://discordapp.com/developers/docs/topics/gateway#resumed

import de.comroid.crystalshard.adapter.MainAPI;
import de.comroid.crystalshard.util.model.serialization.JSONBindingLocation;

@MainAPI
@JSONBindingLocation(RESUMED.JSON.class)
public interface RESUMED extends GatewayEventBase {
    interface JSON {}
}
