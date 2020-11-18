package org.comroid.crystalshard.entity;

public interface Snowflake {
    long getID();

    EntityType getEntityType();

    default boolean equals(Snowflake other) {
        return getEntityType().equals(other.getEntityType())
                && getID() == other.getID();
    }
}
