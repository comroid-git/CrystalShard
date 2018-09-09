package de.kaleidox.crystalshard.internal.event.message.reaction;

import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.event.EventBase;
import de.kaleidox.crystalshard.main.handling.event.message.reaction.ReactionAddEvent;
import de.kaleidox.crystalshard.main.handling.types.MessageAttachingEvent;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.items.message.reaction.Reaction;
import de.kaleidox.crystalshard.main.items.server.emoji.Emoji;
import de.kaleidox.crystalshard.main.items.user.User;

public class ReactionAddEventInternal extends EventBase implements ReactionAddEvent {
    private final Reaction reaction;
    private final int newReactionCount;
    private final Message message;

    public ReactionAddEventInternal(
            DiscordInternal discordInternal,
            Reaction reaction, int newReactionCount, Message message) {
        super(discordInternal, MessageAttachingEvent.REACTION_ADD);
        this.reaction = reaction;
        this.newReactionCount = newReactionCount;
        this.message = message;
    }

    @Override
    public Reaction getReaction() {
        return reaction;
    }

    @Override
    public Emoji getEmoji() {
        return reaction.getEmoji();
    }

    @Override
    public User getUser() {
        return reaction.getUser();
    }

    @Override
    public Message getMessage() {
        return message;
    }
}
