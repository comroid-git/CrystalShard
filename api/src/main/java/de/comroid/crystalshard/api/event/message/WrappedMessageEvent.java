package de.comroid.crystalshard.api.event.message;

import java.util.Optional;

import de.comroid.crystalshard.api.entity.message.Message;

import org.jetbrains.annotations.Nullable;

public interface WrappedMessageEvent extends MessageEvent {
    @Override
    default @Nullable Message getTriggeringMessage() {
        return wrapTriggeringMessage().orElse(null);
    }

    Optional<Message> wrapTriggeringMessage();
}
