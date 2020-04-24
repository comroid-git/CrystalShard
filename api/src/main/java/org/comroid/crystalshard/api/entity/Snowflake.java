package org.comroid.crystalshard.api.entity;

import java.time.Instant;
import java.util.Comparator;

import org.comroid.crystalshard.CrystalShard;
import org.comroid.crystalshard.api.DiscordBot;
import org.comroid.uniform.node.UniValueNode;
import org.comroid.varbind.GroupBind;
import org.comroid.varbind.VarBind;
import org.comroid.varbind.VarCarrier;

import org.jetbrains.annotations.NotNull;

public interface Snowflake extends Comparable<Snowflake>, VarCarrier<DiscordBot> {
    Comparator<Snowflake> SNOWFLAKE_COMPARATOR = Comparator.comparingLong(flake -> flake.getID() >> 22);
    long                  DISCORD_EPOCH        = 1420070400000L;

    default Instant getCreationTimestamp() {
        return Instant.ofEpochMilli((getID() >> 22) + DISCORD_EPOCH);
    }

    default long getID() {
        return requireNonNull(Bind.ID);
    }

    @Override
    default int compareTo(@NotNull Snowflake other) {
        return SNOWFLAKE_COMPARATOR.compare(this, other);
    }

    interface Bind {
        GroupBind         Root = new GroupBind(CrystalShard.SERIALIZATION_ADAPTER, "snowflake");
        VarBind.Uno<Long> ID   = Root.bind1stage("id", UniValueNode.ValueType.LONG);
    }
}
