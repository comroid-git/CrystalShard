package org.comroid.crystalshard.model.message;

import org.comroid.common.ref.Named;
import org.comroid.crystalshard.entity.guild.CustomEmoji;
import org.comroid.crystalshard.entity.message.Message;
import org.comroid.crystalshard.entity.user.User;
import org.comroid.crystalshard.model.emoji.Emoji;
import org.comroid.crystalshard.model.emoji.UnicodeEmoji;
import org.comroid.mutatio.ref.Reference;
import org.jetbrains.annotations.ApiStatus.Internal;

import java.util.HashSet;
import java.util.Set;

public final class ReactionBox implements Named {
    private final Reference<Message> messageRef;
    private final Reference<? extends Emoji> emojiRef;
    private final Set<User> users = new HashSet<>();

    public Message getMessage() {
        return messageRef.requireNonNull("Message missing");
    }

    public Emoji getEmoji() {
        return emojiRef.requireNonNull("Emoji missing");
    }

    public int getCount() {
        return users.size();
    }

    @Override
    public String getName() {
        return String.format("%s %d", emojiRef.requireNonNull(), getCount());
    }

    private ReactionBox(Reference<Message> messageRef, Reference<CustomEmoji> emojiRef) {
        this.messageRef = messageRef;
        this.emojiRef = emojiRef;
    }

    private ReactionBox(Reference<Message> messageRef, UnicodeEmoji emoji) {
        this.messageRef = messageRef;
        this.emojiRef = Reference.constant(emoji);
    }

    @Internal
    public boolean increase(User user) {
        return users.add(user);
    }

    @Internal
    public boolean decrease(User user) {
        return users.remove(user);
    }

    @Internal
    public void clear() {
        users.clear();
    }
}
