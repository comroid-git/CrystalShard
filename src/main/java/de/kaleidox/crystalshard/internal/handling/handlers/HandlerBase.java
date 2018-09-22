package de.kaleidox.crystalshard.internal.handling.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.items.channel.ChannelCategoryInternal;
import de.kaleidox.crystalshard.internal.items.channel.ChannelInternal;
import de.kaleidox.crystalshard.internal.items.message.MessageInternal;
import de.kaleidox.crystalshard.internal.items.role.RoleInternal;
import de.kaleidox.crystalshard.internal.items.server.ServerInternal;
import de.kaleidox.crystalshard.internal.items.user.UserInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.handling.listener.DiscordAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.Listener;
import de.kaleidox.crystalshard.main.handling.listener.channel.ChannelAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.message.MessageAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.server.ServerAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.server.role.RoleAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.user.UserAttachableListener;
import de.kaleidox.crystalshard.main.items.channel.Channel;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.items.role.Role;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.user.User;
import de.kaleidox.logging.Logger;
import de.kaleidox.util.annotations.MayContainNull;
import de.kaleidox.util.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public abstract class HandlerBase {
    private final static Package                                handlerPackage = HandlerBase.class.getPackage();
    private final static ConcurrentHashMap<String, HandlerBase> instances      = new ConcurrentHashMap<>();
    final static         Logger                                 baseLogger     = new Logger(HandlerBase.class);
    
    public abstract void handle(DiscordInternal discord, JsonNode data);
    
// Static members
    // Static membe
    @SuppressWarnings("unchecked")
    public static <T extends HandlerBase> void tryHandle(DiscordInternal discord, JsonNode data) {
        T handler;
        String type = data.path("t").asText("");
        
        if (instances.containsKey(type)) {
            handler = (T) instances.get(type);
            discord.getThreadPool().execute(() -> handler.handle(discord, data.get("d")));
        } else if (!type.isBlank() && !type.isEmpty()) {
            try {
                Class<T> tClass = (Class<T>) Class.forName(handlerPackage.getName() + "." + type);
                handler = tClass.getConstructor().newInstance();
                instances.put(type, handler);
                discord.getThreadPool().execute(() -> {
                    baseLogger.trace("Dispatching event '" + data.get("t").asText() + "' with body: " +
                                     data.get("d").toString());
                    handler.handle(discord, data.get("d"));
                });
            } catch (ClassNotFoundException e) {
                baseLogger.error("Failed to dispatch unknown type: " + data.get("t"));
            } catch (Exception e) {
                baseLogger.exception(e, "Failed to create instance of handler: " + data.get("t"));
            }
        }
    }
    
    @SuppressWarnings("ConstantConditions")
    static <L extends Listener> List<L> collectListeners(
            @NotNull Class<L> listenerClass, @MayContainNull Object... collectIn) {
        Objects.requireNonNull(listenerClass);
        List<L> list = new ArrayList<>();
        
        for (Object obj : collectIn) {
            if (Objects.nonNull(obj)) {
                if (DiscordAttachableListener.class.isAssignableFrom(listenerClass) && obj instanceof Discord) {
                    ((DiscordInternal) obj).getAttachedListeners().stream().filter(listener -> listener.canCastTo(
                            listenerClass)).map(listenerClass::cast).forEachOrdered(list::add);
                }
                
                if (ServerAttachableListener.class.isAssignableFrom(listenerClass) && obj instanceof Server) {
                    ServerInternal serverInternal = (ServerInternal) obj;
                    serverInternal.getListeners().stream().filter(listener -> listener.canCastTo(listenerClass)).map(
                            listenerClass::cast).forEachOrdered(list::add);
                }
                
                if (ChannelAttachableListener.class.isAssignableFrom(listenerClass) && obj instanceof Channel) {
                    switch (((Channel) obj).getType()) {
                        case DM:
                        case GROUP_DM:
                        case GUILD_TEXT:
                        case GUILD_VOICE:
                            ChannelInternal channel = (ChannelInternal) obj;
                            channel.getAttachedListeners()
                                    .stream()
                                    .filter(listener -> listener.canCastTo(listenerClass))
                                    .map(listenerClass::cast)
                                    .forEachOrdered(list::add);
                            break;
                        case GUILD_CATEGORY:
                            ChannelCategoryInternal category = (ChannelCategoryInternal) obj;
                            category.getAttachedListeners()
                                    .stream()
                                    .filter(listener -> listener.canCastTo(listenerClass))
                                    .map(listenerClass::cast)
                                    .forEachOrdered(list::add);
                            break;
                        case UNKNOWN:
                            break;
                    }
                }
                
                if (UserAttachableListener.class.isAssignableFrom(listenerClass) && obj instanceof User) {
                    UserInternal user = (UserInternal) obj;
                    user.getAttachedListeners().stream().filter(listener -> listener.canCastTo(listenerClass)).map(
                            listenerClass::cast).forEachOrdered(list::add);
                }
                
                if (RoleAttachableListener.class.isAssignableFrom(listenerClass) && obj instanceof Role) {
                    RoleInternal role = (RoleInternal) obj;
                    role.getAttachedListeners().stream().filter(listener -> listener.canCastTo(listenerClass)).map(
                            listenerClass::cast).forEachOrdered(list::add);
                }
                
                if (MessageAttachableListener.class.isAssignableFrom(listenerClass) && obj instanceof Message) {
                    MessageInternal message = (MessageInternal) obj;
                    message.getAttachedListeners().stream().filter(listener -> listener.canCastTo(listenerClass)).map(
                            listenerClass::cast).forEachOrdered(list::add);
                }
            }
        }
        
        return list;
    }
}
