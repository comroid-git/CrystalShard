package de.kaleidox.crystalshard.api.entity;

import java.time.Instant;

import de.kaleidox.crystalshard.api.model.ApiBound;
import de.kaleidox.crystalshard.util.annotation.IntroducedBy;
import de.kaleidox.crystalshard.util.model.serialization.JsonDeserializable;
import de.kaleidox.crystalshard.util.model.serialization.JsonTrait;

import com.fasterxml.jackson.databind.JsonNode;

import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;
import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.PRODUCTION;
import static de.kaleidox.crystalshard.util.model.serialization.JsonTrait.identity;

public interface Snowflake extends ApiBound, JsonDeserializable {
    @IntroducedBy(PRODUCTION)
    EntityType getEntityType();

    @IntroducedBy(API)
    default long getID() {
        return getTraitValue(Trait.ID);
    }

    @IntroducedBy(API)
    default Instant getCreationTimestamp() {
        return Instant.ofEpochMilli((getID() >> 22) + 1420070400000L);
    }

    interface Trait {
        JsonTrait<Long, Long> ID = identity(JsonNode::asLong, "id");
    }
}
