package org.comroid.crystalshard.entity;

import org.comroid.crystalshard.DiscordAPI;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.ApiStatus.NonExtendable;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Comparator;

public interface Snowflake extends Comparable<Snowflake> {
    Comparator<Snowflake> SNOWFLAKE_COMPARATOR = Comparator.comparingLong(flake -> flake.getID() >> 22);
    @Language("RegExp")
    String ID_REGEX = "\\d{12,20}"; //todo Improve Regex

    @NonExtendable
    default Instant getCreationTimestamp() {
        return Instant.ofEpochMilli((getID() >> 22) + DiscordAPI.EPOCH);
    }

    long getID();

    @Override
    @NonExtendable
    default int compareTo(@NotNull Snowflake other) {
        return SNOWFLAKE_COMPARATOR.compare(this, other);
    }
}
