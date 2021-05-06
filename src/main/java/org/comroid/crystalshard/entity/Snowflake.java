package org.comroid.crystalshard.entity;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.Context;
import org.comroid.crystalshard.DiscordAPI;
import org.comroid.crystalshard.model.DiscordDataContainer;
import org.comroid.mutatio.model.Ref;
import org.comroid.uniform.node.UniNode;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.util.StandardValueType;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.comroid.varbind.container.DataContainerBase;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.function.BiFunction;

public interface Snowflake extends DiscordDataContainer {
    GroupBind<Snowflake> BASETYPE = new GroupBind<>(DiscordAPI.SERIALIZATION, "snowflake");
    VarBind<Snowflake, Long, Long, Long> ID
            = BASETYPE.createBind("id")
            .extractAs(StandardValueType.LONG)
            .asIdentities()
            .onceEach()
            .setRequired()
            .build();
    @Language("RegExp")
    String ID_REGEX = "\\d{12,32}";

    long getID();

    EntityType<? extends Snowflake> getEntityType();

    default Instant getCreationTimestamp() {
        return Instant.ofEpochMilli((getID() >> 22) + 1420070400000L);
    }

    @Internal
    static <R extends Snowflake> R resolve(
            ContextualProvider ctx,
            UniNode data,
            BiFunction<SnowflakeCache, @NotNull Long, Ref<R>> fineResolver,
            BiFunction<Context, UniObjectNode, R> fineConstructor
    ) {
        final Context context = ctx.as(Context.class, "Bad context");
        return fineResolver.apply(
                context.getCache(),
                data.isValueNode() || !data.has(ID)
                        ? data.asLong(0)
                        : ID.getFrom(data.asObjectNode()))
                .peek(it -> it.updateFrom(data.asObjectNode()))
                .orElseGet(() -> {
                    if (data.isValueNode())
                        return null;
                    return fineConstructor.apply(context, data.asObjectNode());
                });
    }

    default boolean equals(Snowflake other) {
        return getEntityType().equals(other.getEntityType())
                && getID() == other.getID();
    }

    @Internal
    abstract class Abstract extends DataContainerBase<DiscordDataContainer> implements Snowflake {
        private final EntityType<? extends Snowflake> entityType;

        @Override
        public final long getID() {
            return getComputedReference(ID).assertion();
        }

        @Override
        public final EntityType<? extends Snowflake> getEntityType() {
            return entityType;
        }

        protected Abstract(ContextualProvider context, UniObjectNode data, EntityType<? extends Snowflake> entityType) {
            super(context, data);

            context.requireFromContext(SnowflakeCache.class)
                    .getReference(entityType, getID())
                    .set(this);

            this.entityType = entityType;
        }
    }
}
