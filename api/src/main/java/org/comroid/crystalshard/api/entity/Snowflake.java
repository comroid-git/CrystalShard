package org.comroid.crystalshard.api.entity;

import java.time.Instant;
import java.util.Comparator;

import org.comroid.crystalshard.adapter.Adapter;
import org.comroid.crystalshard.api.model.ApiBound;
import org.comroid.crystalshard.api.model.EntityType;
import org.comroid.crystalshard.util.annotation.IntroducedBy;
import org.comroid.varbind.GroupBind;
import org.comroid.varbind.VarBind;
import org.comroid.varbind.VarBind.Location;
import org.comroid.varbind.VarBind.Root;
import org.comroid.varbind.VarCarrier;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.flogger.FluentLogger;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static org.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;
import static org.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.PRODUCTION;
import static org.comroid.uniform.data.impl.json.fastjson.FastJSONLib.fastJsonLib;

@Location(Snowflake.Bind.class)
public interface Snowflake
        extends VarCarrier<JSON, JSONObject, JSONArray>, ApiBound, Comparable<Snowflake> {
    @Internal FluentLogger SNOWFLAKE_COMMON_LOGGER = FluentLogger.forEnclosingClass();

    Comparator<Snowflake> SNOWFLAKE_COMPARATOR = Comparator.comparingLong(flake -> flake.getID() >> 22);

    static Snowflake mime(final long id) {
        return Adapter.require(Snowflake.class, id);
    }

    @IntroducedBy(PRODUCTION)
    EntityType getEntityType();

    @IntroducedBy(API)
    default Instant getCreationTimestamp() {
        return Instant.ofEpochMilli((getID() >> 22) + 1420070400000L);
    }

    @IntroducedBy(API)
    default Long getID() {
        return getVar(Bind.ID);
    }

    @Override
    @Contract(pure = true)
    default int compareTo(@NotNull Snowflake other) {
        return SNOWFLAKE_COMPARATOR.compare(this, other);
    }

    interface Bind {
        @Root
        GroupBind<JSON, JSONObject, JSONArray> Group = new GroupBind<>(fastJsonLib, "snowflake");
        VarBind.Uno<JSONObject, Long>          ID    = Group.bind1Stage("id", Long.class);
    }
}
