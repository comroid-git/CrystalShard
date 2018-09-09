package de.kaleidox.crystalshard.internal.core.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.items.channel.ChannelCategoryInternal;
import de.kaleidox.crystalshard.internal.items.channel.ChannelInternal;
import de.kaleidox.crystalshard.internal.items.message.MessageInternal;
import de.kaleidox.crystalshard.internal.items.server.ServerInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.handling.listener.DiscordAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.Listener;
import de.kaleidox.crystalshard.main.handling.listener.channel.ChannelAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.message.MessageAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.server.ServerAttachableListener;
import de.kaleidox.crystalshard.main.items.DiscordItem;
import de.kaleidox.crystalshard.main.items.channel.Channel;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.logging.Logger;
import de.kaleidox.util.annotations.MayContainNull;
import de.kaleidox.util.annotations.NotNull;
import de.kaleidox.util.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public abstract class HandlerBase {
    final static Logger baseLogger = new Logger(HandlerBase.class);
    private final static ConcurrentHashMap<String, HandlerBase> types = new ConcurrentHashMap<>();

    public abstract void handle(DiscordInternal discord, JsonNode data);

    public static <T extends HandlerBase> void tryHandle(DiscordInternal discord, JsonNode data) {
        T value;
        String type = data.path("t").asText("");

        if (types.containsKey(type)) {
            value = (T) types.get(type);
            discord.getThreadPool().execute(() -> value.handle(discord, data.get("d")));
        } else if (!type.isBlank() && !type.isEmpty()) {
            try {
                Package handlerPackage = HandlerBase.class.getPackage();
                String t1 = handlerPackage.getName() + "." + type;
                Class<T> tClass = (Class<T>) Class.forName(t1);
                value = tClass.getConstructor().newInstance();
                types.put(type, value);
                discord.getThreadPool().execute(() -> {
                    baseLogger.trace("Dispatching event '" + data.get("t").asText() +
                            "' with body: " + data.get("d").toString());
                    value.handle(discord, data.get("d"));
                });
            } catch (ClassNotFoundException e) {
                baseLogger.exception(e, "Failed to dispatch unknown type: " + data.get("t"));
            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                baseLogger.exception(e, "Failed to create instance of handler: " + data.get("t"));
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    static <L extends Listener> List<L> collectListeners(@NotNull Class<L> listenerClass,
                                                         @Nullable DiscordItem[] collectFrom,
                                                         @MayContainNull Object... collectIn) {
        List<L> list = new ArrayList<>();
        boolean anyTarget = false;
        if (collectFrom == null) anyTarget = true;

        for (Object obj : collectIn) {
            if (Objects.nonNull(obj)) {
                if (listenerClass.isAssignableFrom(DiscordAttachableListener.class) && obj instanceof Discord) {
                    ((DiscordInternal) obj).getListeners()
                            .stream()
                            .filter(listener -> listener.canCastTo(listenerClass))
                            .map(listenerClass::cast)
                            .forEachOrdered(list::add);
                }

                if (listenerClass.isAssignableFrom(ServerAttachableListener.class) && obj instanceof Server) {
                    ServerInternal serverInternal = (ServerInternal) obj;
                    if (anyTarget) {
                        serverInternal.getListeners()
                                .stream()
                                .filter(listener -> listener.canCastTo(listenerClass))
                                .map(listenerClass::cast)
                                .forEachOrdered(list::add);
                    } else {
                        for (DiscordItem target : collectFrom) {
                            if (target.equals(obj)) {
                                serverInternal.getListeners()
                                        .stream()
                                        .filter(listener -> listener.canCastTo(listenerClass))
                                        .map(listenerClass::cast)
                                        .forEachOrdered(list::add);
                            }
                        }
                    }
                }

                if (listenerClass.isAssignableFrom(ChannelAttachableListener.class) && obj instanceof Channel) {
                    switch (((Channel) obj).getType()) {
                        case DM:
                        case GROUP_DM:
                        case GUILD_TEXT:
                        case GUILD_VOICE:
                            ChannelInternal channel = (ChannelInternal) obj;
                            if (anyTarget) {
                                channel.getListeners()
                                        .stream()
                                        .filter(listener -> listener.canCastTo(listenerClass))
                                        .map(listenerClass::cast)
                                        .forEachOrdered(list::add);
                            } else {
                                for (DiscordItem target : collectFrom) {
                                    if (target.equals(obj)) {
                                        channel.getListeners()
                                                .stream()
                                                .filter(listener -> listener.canCastTo(listenerClass))
                                                .map(listenerClass::cast)
                                                .forEachOrdered(list::add);
                                    }
                                }
                            }
                            break;
                        case GUILD_CATEGORY:
                            ChannelCategoryInternal category = (ChannelCategoryInternal) obj;
                            if (anyTarget) {
                                category.getListeners()
                                        .stream()
                                        .filter(listener -> listener.canCastTo(listenerClass))
                                        .map(listenerClass::cast)
                                        .forEachOrdered(list::add);
                            } else {
                                for (DiscordItem target : collectFrom) {
                                    if (target.equals(obj)) {
                                        category.getListeners()
                                                .stream()
                                                .filter(listener -> listener.canCastTo(listenerClass))
                                                .map(listenerClass::cast)
                                                .forEachOrdered(list::add);
                                    }
                                }
                            }
                            break;
                        case UNKNOWN:
                            break;
                    }
                }

                if (listenerClass.isAssignableFrom(MessageAttachableListener.class) && obj instanceof Message) {
                    MessageInternal message = (MessageInternal) obj;
                    if (anyTarget) {
                        message.getListeners()
                                .stream()
                                .filter(listener -> listener.canCastTo(listenerClass))
                                .map(listenerClass::cast)
                                .forEachOrdered(list::add);
                    } else {
                        for (DiscordItem target : collectFrom) {
                            if (target.equals(obj)) {
                                message.getListeners()
                                        .stream()
                                        .filter(listener -> listener.canCastTo(listenerClass))
                                        .map(listenerClass::cast)
                                        .forEachOrdered(list::add);
                            }
                        }
                    }
                }
            }
        }

        return list;
    }
}
