package org.comroid.crystalshard.model;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.DiscordAPI;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.container.DataContainerBase;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractDataContainer
        extends DataContainerBase<AbstractDataContainer>
        implements ContextualProvider.Underlying {
    public static final GroupBind<AbstractDataContainer> BASETYPE
            = new GroupBind<>(DiscordAPI.SERIALIZATION, "data-container");
    private final ContextualProvider context;

    @Override
    public ContextualProvider getUnderlyingContextualProvider() {
        return context.plus(this);
    }

    public AbstractDataContainer(ContextualProvider context, @Nullable UniObjectNode initialData) {
        super(initialData);

        this.context = context;
    }
}
