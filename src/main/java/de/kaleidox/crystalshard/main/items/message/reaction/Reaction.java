package de.kaleidox.crystalshard.main.items.message.reaction;

import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.items.server.emoji.Emoji;
import de.kaleidox.crystalshard.main.items.user.User;

public interface Reaction {
    Discord getDiscord();

    Emoji getEmoji();

    User getUser();

    Message getMessage();
}
