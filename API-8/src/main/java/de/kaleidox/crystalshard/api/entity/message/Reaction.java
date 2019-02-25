package de.kaleidox.crystalshard.api.entity.message;

import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.entity.message.Message;
import de.kaleidox.crystalshard.api.entity.server.emoji.Emoji;
import de.kaleidox.crystalshard.api.entity.user.User;

public interface Reaction {
    Discord getDiscord();

    Emoji getEmoji();

    User getUser();

    Message getMessage();

    /**
     * Get the new count of the reaction. Currently does not work properly.
     *
     * @return The count of the reaction.
     */
    int getCount();

    void remove();
}
