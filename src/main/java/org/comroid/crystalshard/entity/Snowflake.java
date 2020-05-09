package org.comroid.crystalshard.entity;

import org.comroid.crystalshard.CrystalShard;
import org.comroid.crystalshard.DiscordAPI;
import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.model.BotBound;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.uniform.node.UniValueNode;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.comroid.varbind.container.DataContainer;
import org.comroid.varbind.container.DataContainerBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Comparator;
import java.util.Objects;

public interface Snowflake extends BotBound, Comparable<Snowflake>, DataContainer<DiscordBot> {
    Comparator<Snowflake> SNOWFLAKE_COMPARATOR = Comparator.comparingLong(flake -> flake.getID() >> 22);

    default Instant getCreationTimestamp() {
        return Instant.ofEpochMilli((getID() >> 22) + DiscordAPI.EPOCH);
    }

    default long getID() {
        return requireNonNull(Bind.ID);
    }

    @Override
    default int compareTo(@NotNull Snowflake other) {
        return SNOWFLAKE_COMPARATOR.compare(this, other);
    }

    interface Bind {
        GroupBind<Snowflake, DiscordBot> Root = new GroupBind<>(CrystalShard.SERIALIZATION_ADAPTER, "snowflake");
        VarBind.OneStage<Long> ID = Root.bind1stage("id", UniValueNode.ValueType.LONG);
    }

    abstract class Base extends BotBound.DataBase implements Snowflake {
        protected Base(@Nullable UniObjectNode initialData, @NotNull DiscordBot dependencyObject) {
            super(initialData, dependencyObject);
        }
    }
}
