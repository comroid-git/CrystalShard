package de.comroid.crystalshard.api.entity;

import java.time.Instant;
import java.util.Comparator;

import de.comroid.crystalshard.adapter.Adapter;
import de.comroid.crystalshard.api.model.ApiBound;
import de.comroid.crystalshard.util.annotation.IntroducedBy;
import de.comroid.crystalshard.util.model.serialization.JsonDeserializable;
import de.comroid.crystalshard.util.model.serialization.JSONBinding;

import com.alibaba.fastjson.JSONObject;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;
import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.PRODUCTION;
import static de.comroid.crystalshard.util.model.serialization.JSONBinding.identity;

public interface Snowflake extends ApiBound, JsonDeserializable, Comparable<Snowflake> {
    Comparator<Snowflake> SNOWFLAKE_COMPARATOR = Comparator.comparingLong(flake -> flake.getID() >> 22);

    @IntroducedBy(PRODUCTION)
    EntityType getEntityType();

    @IntroducedBy(API)
    default long getID() {
        return getBindingValue(JSON.ID);
    }

    @IntroducedBy(API)
    default Instant getCreationTimestamp() {
        return Instant.ofEpochMilli((getID() >> 22) + 1420070400000L);
    }

    @Override
    @Contract(pure = true)
    default int compareTo(@NotNull Snowflake other) {
        return SNOWFLAKE_COMPARATOR.compare(this, other);
    }

    static Snowflake mime(final long id) {
        return Adapter.require(Snowflake.class, id);
    }

    interface JSON {
        JSONBinding.OneStage<Long> ID = identity("id", JSONObject::getLong);
    }
}