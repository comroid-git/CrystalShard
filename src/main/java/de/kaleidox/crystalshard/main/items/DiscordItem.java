package de.kaleidox.crystalshard.main.items;

import de.kaleidox.crystalshard.main.Discord;

/**
 * This class represents any Discord element that has an ID.
 */
public interface DiscordItem {

    /**
     * Gets the ID of the item.
     *
     * @return The ID of the item.
     */
    long getId();

    /**
     * Gets the Discord object of the item.
     *
     * @return The Discord object of the item.
     */
    Discord getDiscord();
}
