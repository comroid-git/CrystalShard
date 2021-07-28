package org.comroid.crystalshard.model;

import org.comroid.crystalshard.Context;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.container.DataContainer;

public interface DiscordDataContainer extends DataContainer<DiscordDataContainer>, Context {
    GroupBind<AbstractDataContainer> BASETYPE
            = new GroupBind<>("data-container");
}
