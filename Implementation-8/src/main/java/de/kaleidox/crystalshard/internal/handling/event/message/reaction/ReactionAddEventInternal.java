package de.kaleidox.crystalshard.internal.handling.event.message.reaction;

import de.kaleidox.crystalshard.api.entity.message.Message;
import de.kaleidox.crystalshard.api.entity.message.Reaction;
import de.kaleidox.crystalshard.api.entity.server.emoji.Emoji;
import de.kaleidox.crystalshard.api.entity.user.User;
import de.kaleidox.crystalshard.api.handling.event.message.reaction.ReactionAddEvent;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.EventBase;

public class ReactionAddEventInternal extends EventBase implements ReactionAddEvent {
    private final Reaction reaction;
    private final Message message;

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
    public int getCount() {
        return reaction.getCount();
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
