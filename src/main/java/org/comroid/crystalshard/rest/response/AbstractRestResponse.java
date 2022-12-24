package org.comroid.crystalshard.rest.response;

import org.comroid.api.ContextualProvider;
import org.comroid.uniform.node.UniNode;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractRestResponse extends DataContainerBase<DiscordDataContainer> implements DiscordDataContainer {
    protected AbstractRestResponse(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);
    }
}
