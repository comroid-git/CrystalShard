package de.comroid.crystalshard.api.event.multipart.message;

import java.util.Optional;

import de.comroid.crystalshard.api.entity.message.Message;
import de.comroid.crystalshard.api.event.multipart.APIEvent;

import org.jetbrains.annotations.Nullable;

public interface WrappedMessageEvent extends MessageEvent, APIEvent {
    @Override
    default @Nullable Message getMessage() {
        return wrapMessage().orElse(null);
    }

    Optional<Message> wrapMessage(); 
}