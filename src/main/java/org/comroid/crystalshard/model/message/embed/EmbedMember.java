package org.comroid.crystalshard.model.message.embed;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.model.AbstractDataContainer;
import org.comroid.uniform.node.UniNode;
import org.jetbrains.annotations.Nullable;

public abstract class EmbedMember extends AbstractDataContainer {
    protected final Embed parent;

    public Embed getParent() {
        return parent;
    }

    public EmbedMember(Embed parent, ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);

        this.parent = parent;
    }
}
