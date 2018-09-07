package de.kaleidox.crystalshard.internal.items.message.reaction;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.items.server.ServerInternal;
import de.kaleidox.crystalshard.internal.items.server.emoji.CustomEmojiInternal;
import de.kaleidox.crystalshard.internal.items.server.emoji.UnicodeEmojiInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.items.channel.ServerChannel;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.items.message.reaction.Reaction;
import de.kaleidox.crystalshard.main.items.server.emoji.Emoji;
import de.kaleidox.crystalshard.main.items.user.User;
import de.kaleidox.logging.Logger;

public class ReactionInternal implements Reaction {
    private final static Logger logger = new Logger(ReactionInternal.class);
    private final DiscordInternal discord;
    private final int count;
    private final boolean me;
    private final Emoji emoji;
    private final Message message;

    public ReactionInternal(DiscordInternal discord, Message message, JsonNode data) {
        logger.deeptrace("Creating Reaction object for data: " + data);
        this.discord = discord;
        this.message = message;
        this.count = data.path("count").asInt(0);
        this.me = data.path("me").asBoolean(false);
        //noinspection OptionalGetWithoutIsPresent
        this.emoji = data.get("emoji").has("id") ?
                new CustomEmojiInternal(discord,
                        (ServerInternal) message.getChannel().toServerChannel().map(ServerChannel::getServer).get(),
                        data.get("emoji"), true) :
                new UnicodeEmojiInternal(discord, data.get("emoji"), true);
    }

    @Override
    public Discord getDiscord() {
        return discord;
    }

    @Override
    public Emoji getEmoji() {
        return emoji;
    }

    @Override
    public User getUser() {
        return null; // todo
    }

    @Override
    public Message getMessage() {
        return message;
    }
}
