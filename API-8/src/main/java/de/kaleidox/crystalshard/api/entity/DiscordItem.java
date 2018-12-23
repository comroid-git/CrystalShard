package de.kaleidox.crystalshard.api.entity;

import de.kaleidox.crystalshard.api.Discord;

/**
 * This class represents any Discord element that has an ID.
 */
public interface DiscordItem {
    /**
     * Gets the Discord object of the item.
     *
     * @return The Discord object of the item.
     */
    Discord getDiscord();

    /**
     * Used for comparing two DiscordItems after their ID.
     *
     * @param other The DiscordItem to equals to.
     * @return Whether the items are equal.
     * @see Object#equals(Object)
     */
    default boolean equals(DiscordItem other) {
        if (other == null) return false;
        return (this.getId() == other.getId());
    }

    /**
     * Gets the ID of the item.
     *
     * @return The ID of the item.
     */
    long getId();
}
