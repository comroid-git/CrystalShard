package org.comroid.crystalshard.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.intellij.lang.annotations.Language;

import java.time.Instant;

@AllArgsConstructor
public abstract class Snowflake {
    @Id
    @Getter
    private long ID;
    @Column
    @Getter
    private EntityType type;

    public final Instant getCreationTimestamp() {
        return Instant.ofEpochMilli((getID() >> 22) + 1420070400000L);
    }

    public final boolean equals(Snowflake other) {
        return //getEntityType().equals(other.getEntityType()) &&
                getID() == other.getID();
    }
}
