package de.kaleidox.crystalshard.internal.items.message.reaction;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.items.server.emoji.UnicodeEmojiInternal;
import de.kaleidox.crystalshard.logging.Logger;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.items.channel.ServerChannel;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.items.message.reaction.Reaction;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.server.emoji.Emoji;
import de.kaleidox.crystalshard.main.items.user.User;
import de.kaleidox.crystalshard.util.annotations.NotNull;
import de.kaleidox.crystalshard.util.annotations.Nullable;
import de.kaleidox.crystalshard.util.helpers.MapHelper;

import java.util.concurrent.ConcurrentHashMap;

public class ReactionInternal implements Reaction {
    private final static ConcurrentHashMap<ReactionCriteria, Reaction> instances = new ConcurrentHashMap<>();
    private final static ConcurrentHashMap<ReactionCriteria, Integer>  counts    = new ConcurrentHashMap<>();
    private final static Logger                                        logger    = new Logger(ReactionInternal.class);
    private final        Discord                                       discord;
    private final        boolean                                       me;
    private final        Emoji                                         emoji;
    private final        Message                                       message;
    private final        User                                          user;
    
    ReactionInternal(Discord discord, Message message, User user, JsonNode data) {
        logger.deeptrace("Creating Reaction object for data: " + data);
        this.discord = discord;
        this.message = message;
        this.user = user;
        this.me = data.path("me").asBoolean(false);
        this.emoji = data.get("emoji").get("id").isNull() ? new UnicodeEmojiInternal(discord, data.get("emoji"), true) : discord.getEmojiCache().getOrCreate(
                discord,
                message.getChannel().toServerChannel().map(ServerChannel::getServer).orElseThrow(AssertionError::new),
                data.get("emoji"),
                true);
        
        ReactionCriteria criteria = new ReactionCriteria(message, emoji);
        instances.put(criteria, this);
        counts.put(criteria, data.path("count").asInt(0));
    }
    
    // Override Methods
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
        ReactionCriteria criteria = new ReactionCriteria(message, emoji);
        return counts.getOrDefault(criteria, 0);
    }
    
    @Override
    public String toString() {
        return "Reaction at " + message + " with " + emoji;
    }
    
    private Reaction changeCount(int delta) {
        ReactionCriteria criteria = new ReactionCriteria(message, emoji);
        counts.put(criteria, (delta == Integer.MIN_VALUE ? 0 : (counts.getOrDefault(criteria, 0) + delta)));
        return this;
    }
    
// Static membe
    public static Reaction getInstance(@Nullable Server server, @NotNull Message message, @Nullable User user, @NotNull JsonNode data, int delta) {
        Emoji emoji = Emoji.of(message.getDiscord(), server, data.get("emoji"));
        ReactionCriteria criteria = new ReactionCriteria(message, emoji);
        return ((ReactionInternal) MapHelper.getEquals(instances,
                                                       criteria,
                                                       new ReactionInternal(message.getDiscord(), message, user, data))).changeCount(delta);
    }
    
    private static class ReactionCriteria {
        private final Message message;
        private final Emoji   emoji;
        
        public ReactionCriteria(Message message, Emoji emoji) {
            this.message = message;
            this.emoji = emoji;
        }
        
        // Override Methods
        @Override
        public boolean equals(Object obj) {
            if (obj instanceof ReactionCriteria) return (((ReactionCriteria) obj).message.equals(message)) && ((ReactionCriteria) obj).emoji.equals(emoji);
            return false;
        }
    }
}
