package de.kaleidox.crystalshard.internal.handling.event.message.reaction;

import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.EventBase;
import de.kaleidox.crystalshard.main.handling.event.message.reaction.ReactionAddEvent;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.items.message.reaction.Reaction;
import de.kaleidox.crystalshard.main.items.server.emoji.Emoji;
import de.kaleidox.crystalshard.main.items.user.User;

public class ReactionAddEventInternal extends EventBase implements ReactionAddEvent {
    private final Reaction reaction;
    private final Message  message;

    public ReactionAddEventInternal(DiscordInternal discordInternal, Reaction reaction, Message message) {
        super(discordInternal);
        this.reaction = reaction;
        this.message = message;
    }

// Override Methods
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
    public int getCount() {
        return reaction.getCount();
    }

    @Override
    public Message getMessage() {
        return message;
    }
}
