package org.comroid.crystalshard.entity;

import org.comroid.crystalshard.CrystalShard;
import org.comroid.crystalshard.DiscordAPI;
import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.core.cache.SnowflakeCache;
import org.comroid.crystalshard.core.cache.SnowflakeSelector;
import org.comroid.crystalshard.model.BotBound;
import org.comroid.uniform.cache.Cache;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.uniform.node.UniValueNode;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.comroid.varbind.container.DataContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Comparator;

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
        protected Base(DiscordBot bot, @Nullable UniObjectNode initialData) {
            super(initialData, bot);

            final long id = getID();
            final SnowflakeCache entityCache = bot.getCache();
            final SnowflakeSelector sel = entityCache.getReference(id, true)
                    .requireNonNull("Assertion Failure");

            sel.put(this);
        }
    }
}
