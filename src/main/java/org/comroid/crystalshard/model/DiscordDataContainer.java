package org.comroid.crystalshard.model;

import org.comroid.crystalshard.Context;
import org.comroid.crystalshard.DiscordAPI;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.container.DataContainer;

public interface DiscordDataContainer extends DataContainer<DiscordDataContainer>, Context {
    GroupBind<AbstractDataContainer> BASETYPE
            = new GroupBind<>(DiscordAPI.SERIALIZATION, "data-container");
}
