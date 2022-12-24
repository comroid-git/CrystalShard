package org.comroid.crystalshard.model.message.embed;

import org.comroid.uniform.node.UniNode;
import org.jetbrains.annotations.Nullable;

public abstract class EmbedMember extends DataContainerBase<DiscordDataContainer> implements DiscordDataContainer {
    protected final Embed parent;

    public Embed getEmbed() {
        return parent;
    }

    public EmbedMember(Embed parent, @Nullable UniNode initialData) {
        super(parent, initialData);

        this.parent = parent;
    }
}
