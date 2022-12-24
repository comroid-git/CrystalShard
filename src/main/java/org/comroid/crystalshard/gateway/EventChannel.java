package org.comroid.crystalshard.gateway;

import org.comroid.api.Named;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.SubscribableChannel;

import java.util.HashSet;
import java.util.Set;

public enum EventChannel implements SubscribableChannel, Named {
    Socket,
    Gateway,
    Client;

    private final Logger log = LoggerFactory.getLogger(EventChannel.class.getCanonicalName() + '.' + name());
    private final Set<MessageHandler> handlers = new HashSet<>();

    @Override
    public boolean send(@NotNull Message<?> message, long timeout) {
        log.trace("Handling Message: {}", message);
        int c = 0;
        for (MessageHandler handler : handlers) {
            handler.handleMessage(message);
            c++;
        }
        return c > 0;
    }

    @Override
    public boolean subscribe(@NotNull MessageHandler handler) {
        return handlers.add(handler);
    }

    @Override
    public boolean unsubscribe(@NotNull MessageHandler handler) {
        return handlers.remove(handler);
    }
}
