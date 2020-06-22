package org.comroid.crystalshard.entity;

import org.comroid.crystalshard.CrystalShard;
import org.comroid.crystalshard.DiscordAPI;
import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.model.BotBound;
import org.comroid.uniform.ValueType;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.comroid.varbind.container.DataContainer;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Comparator;

public interface Snowflake extends BotBound, Comparable<Snowflake>, DataContainer<DiscordBot> {
    Comparator<Snowflake> SNOWFLAKE_COMPARATOR = Comparator.comparingLong(flake -> flake.getID() >> 22);
    @Language("RegExp") String ID_REGEX = "\\d{12,20}"; //todo Improve Regex

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
        VarBind<Long, DiscordBot, Long, Long> ID = Root.createBind("id")
                .extractAs(ValueType.LONG)
                .asIdentities()
                .onceEach()
                .build();
    }

    abstract class Base extends BotBound.DataBase implements Snowflake {
        protected Base(DiscordBot bot, @Nullable UniObjectNode initialData) {
            super(bot, initialData);

            final SnowflakeSelector sel = bot.getCache().getReference(getID(), true)
                    .requireNonNull("Assertion Failure");

            sel.put(this);
        }
    }
}
