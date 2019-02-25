package de.kaleidox.crystalshard.api.entity.channel;

import de.kaleidox.crystalshard.api.entity.user.User;

public interface GroupChannel extends TextChannel {
    interface Builder extends Channel.Builder<Builder, GroupChannel> {
        Builder addRecipient(User user);
    }
}
