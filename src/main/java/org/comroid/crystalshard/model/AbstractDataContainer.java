package org.comroid.crystalshard.model;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.Context;
import org.comroid.crystalshard.DiscordAPI;
import org.comroid.uniform.node.UniNode;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.container.DataContainerBase;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractDataContainer
        extends DataContainerBase<AbstractDataContainer>
        implements Context {
    public static final GroupBind<AbstractDataContainer> BASETYPE
            = new GroupBind<>(DiscordAPI.SERIALIZATION, "data-container");

    public AbstractDataContainer(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);
    }
}
