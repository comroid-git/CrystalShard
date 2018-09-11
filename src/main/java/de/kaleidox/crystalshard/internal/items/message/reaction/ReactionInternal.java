package de.kaleidox.crystalshard.internal.items.message.reaction;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.items.server.emoji.CustomEmojiInternal;
import de.kaleidox.crystalshard.internal.items.server.emoji.UnicodeEmojiInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.items.channel.ServerChannel;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.items.message.reaction.Reaction;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.server.emoji.Emoji;
import de.kaleidox.crystalshard.main.items.user.User;
import de.kaleidox.logging.Logger;
import de.kaleidox.util.annotations.NotNull;
import de.kaleidox.util.annotations.Nullable;
import de.kaleidox.util.helpers.MapHelper;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ReactionInternal implements Reaction {
    private final static ConcurrentHashMap<ReactionCriteria, Reaction> instances = new ConcurrentHashMap<>();
    private final static Logger logger = new Logger(ReactionInternal.class);
    private final Discord discord;
    private final AtomicInteger count;
    private final boolean me;
    private final Emoji emoji;
    private final Message message;
    private final User user;

    ReactionInternal(Discord discord, Message message, User user, JsonNode data) {
        logger.deeptrace("Creating Reaction object for data: " + data);
        this.discord = discord;
        this.message = message;
        this.user = user;
        this.count = new AtomicInteger(data.path("count").asInt(0));
        this.me = data.path("me").asBoolean(false);
        this.emoji = data.get("emoji").get("id").isNull() ?
                new UnicodeEmojiInternal(discord, data.get("emoji"), true) :
                CustomEmojiInternal.getInstance(discord,
                        message.getChannel().toServerChannel().map(ServerChannel::getServer).get(),
                        data.get("emoji"), true);

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
        return user;
    }

    @Override
    public Message getMessage() {
        return message;
    }

    @Override
    public int getCount() {
        return count.get();
    }

    private Reaction changeCount(int delta) {
        count.addAndGet(delta);
        return this;
    }

    @Override
    public String toString() {
        return "Reaction at " + message + " with " + emoji;
    }

    public static Reaction getInstance(@Nullable Server server,
                                       @NotNull Message message,
                                       @Nullable User user,
                                       @NotNull JsonNode data,
                                       int delta) {
        Emoji emoji = Emoji.of(message.getDiscord(), server, data.get("emoji"));
        ReactionCriteria criteria = new ReactionCriteria(message, emoji);
        return ((ReactionInternal) MapHelper.getEquals(instances, criteria,
                new ReactionInternal(message.getDiscord(), message, user, data)))
                .changeCount(delta);
    }

    private static class ReactionCriteria {
        private final Message message;
        private final Emoji emoji;

        public ReactionCriteria(Message message, Emoji emoji) {
            this.message = message;
            this.emoji = emoji;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof ReactionCriteria)
                return (((ReactionCriteria) obj).message.equals(message)) &&
                        ((ReactionCriteria) obj).emoji.equals(emoji);
            return false;
        }
    }
}
