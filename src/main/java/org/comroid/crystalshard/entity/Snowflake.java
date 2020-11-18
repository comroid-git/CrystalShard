package org.comroid.crystalshard.entity;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.DiscordAPI;
import org.comroid.mutatio.ref.Reference;
import org.comroid.uniform.ValueType;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.comroid.varbind.container.DataContainer;
import org.comroid.varbind.container.DataContainerBase;
import org.jetbrains.annotations.ApiStatus.Internal;

public interface Snowflake extends DataContainer<Snowflake>, ContextualProvider.Underlying {
    GroupBind<Snowflake> BASETYPE = new GroupBind<>(DiscordAPI.SERIALIZATION, "snowflake");
    VarBind<Snowflake, Long, Long, Long> ID
            = BASETYPE.createBind("id")
            .extractAs(ValueType.LONG)
            .asIdentities()
            .onceEach()
            .setRequired()
            .build();

    long getID();

    EntityType getEntityType();

    default boolean equals(Snowflake other) {
        return getEntityType().equals(other.getEntityType())
                && getID() == other.getID();
    }

    @Internal
    abstract class Abstract extends DataContainerBase<Snowflake> implements Snowflake {
        public final Reference<Long> id = getComputedReference(ID);
        private final EntityType entityType;
        private final ContextualProvider context;

        @Override
        public final long getID() {
            return id.requireNonNull();
        }

        @Override
        public final EntityType getEntityType() {
            return entityType;
        }

        @Override
        public final ContextualProvider getUnderlyingContextualProvider() {
            return context.plus(this);
        }

        protected Abstract(ContextualProvider context, UniObjectNode data, EntityType entityType) {
            super(data);

            this.context = context;
            this.entityType = entityType;
        }
    }
}
