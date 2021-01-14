package org.comroid.crystalshard.rest.response;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.model.AbstractDataContainer;
import org.comroid.uniform.node.UniObjectNode;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractRestResponse extends AbstractDataContainer {
    protected AbstractRestResponse(ContextualProvider context, @Nullable UniObjectNode initialData) {
        super(context, initialData);
    }
}
