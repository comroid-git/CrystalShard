package de.kaleidox.crystalshard.internal.handling.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.util.RoleContainer;
import de.kaleidox.crystalshard.logging.Logger;
import de.kaleidox.crystalshard.main.handling.listener.Listener;
import de.kaleidox.crystalshard.main.handling.listener.ListenerAttachable;
import de.kaleidox.crystalshard.main.handling.listener.ListenerManager;
import de.kaleidox.crystalshard.main.items.role.Role;
import de.kaleidox.util.annotations.MayContainNull;
import de.kaleidox.util.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public abstract class HandlerBase {
    final static Logger baseLogger = new Logger(HandlerBase.class);
    private final static Package handlerPackage = HandlerBase.class.getPackage();
    private final static ConcurrentHashMap<String, HandlerBase> instances = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public static <T extends HandlerBase> void tryHandle(DiscordInternal discord, JsonNode data) {
        T handler;
        String type = data.path("t")
                .asText("");

        if (instances.containsKey(type)) {
            ((T) instances.get(type)).handle(discord, data.get("d"));
        } else if (!type.isEmpty()) {
            try {
                Class<T> tClass = (Class<T>) Class.forName(handlerPackage.getName() + "." + type);
                handler = tClass.getConstructor()
                        .newInstance();
                instances.put(type, handler);
                try {
                    baseLogger.trace("Dispatching event '" + data.get("t")
                            .asText() + "' with body: " + data.get("d")
                            .toString());
                    handler.handle(discord, data.get("d"));
                } catch (Exception e) {
                    baseLogger.exception(e, "Exception in Handler: " + type);
                }
            } catch (ClassNotFoundException e) {
                baseLogger.error("Failed to dispatch unknown type: " + data.get("t"));
            } catch (Exception e) {
                baseLogger.exception(e, "Failed to create instance of handler: " + data.get("t"));
            }
        }
    }

    @SuppressWarnings("unchecked")
    @SafeVarargs
    static <L extends Listener, C extends ListenerAttachable<? super L>> List<L> collectListeners(@NotNull Class<L> listenerClass,
                                                                                                  @MayContainNull C... collectIn) {
        Objects.requireNonNull(listenerClass);
        List<L> collect = new ArrayList<>();

        for (C item : collectIn) {
            if (Objects.nonNull(item)) {
                if (item instanceof RoleContainer) {
                    for (Role role : ((RoleContainer) item).getRoles()) {
                        role.getListenerManagers()
                                .stream()
                                .filter(manager -> listenerClass.isAssignableFrom(manager.getListener()
                                        .getClass()))
                                .filter(ListenerManager::isEnabled)
                                .map(ListenerManager::getListener)
                                .map(listener -> (L) listener) // TODO: 30.10.2018 Test if this cast is safe
                                .forEachOrdered(collect::add);
                    }
                } else item.getListenerManagers()
                        .stream()
                        .filter(manager -> listenerClass.isAssignableFrom(manager.getListener()
                                .getClass()))
                        .filter(ListenerManager::isEnabled)
                        .map(ListenerManager::getListener)
                        .map(listener -> (L) listener)
                        .forEachOrdered(collect::add);
            }
        }

        return collect;
    }

    public abstract void handle(DiscordInternal discord, JsonNode data);
}
