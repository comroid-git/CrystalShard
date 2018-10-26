package de.kaleidox.crystalshard.main.items.channel;

import de.kaleidox.crystalshard.main.items.user.User;

public interface GroupChannel extends TextChannel {
    interface Builder extends Channel.Builder<Builder, GroupChannel> {
        Builder addRecipient(User user);
    }
}
