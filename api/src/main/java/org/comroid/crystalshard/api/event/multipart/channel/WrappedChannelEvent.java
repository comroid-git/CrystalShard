package org.comroid.crystalshard.api.event.multipart.channel;

import java.util.Optional;

import org.comroid.crystalshard.api.entity.channel.Channel;
import org.comroid.crystalshard.api.event.multipart.APIEvent;

import org.jetbrains.annotations.Nullable;

public interface WrappedChannelEvent<C extends Channel> extends ChannelEvent<C>, APIEvent {
    @Override
    default @Nullable C getChannel() {
        return wrapChannel().orElse(null);
    }

    Optional<C> wrapChannel();
}
