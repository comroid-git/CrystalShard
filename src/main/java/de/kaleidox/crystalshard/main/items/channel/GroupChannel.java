package de.kaleidox.crystalshard.main.items.channel;

import de.kaleidox.crystalshard.internal.items.channel.ChannelBuilderInternal;
import de.kaleidox.crystalshard.main.items.user.User;
import java.util.concurrent.CompletableFuture;

public interface GroupChannel extends TextChannel {
    default Builder BUILDER(User... recipients) {
        return new ChannelBuilderInternal.GroupChannelBuilder(recipients);
    }
    
    interface Builder {
        Builder addRecipient(User user);
        
        /**
         * Builds and creates the GroupChannel.
         *
         * @return A future to contain the created GroupChannel.
         */
        CompletableFuture<GroupChannel> build();
    }
}
