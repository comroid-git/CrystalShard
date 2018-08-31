package de.kaleidox.crystalshard.internal.core.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.logging.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ConcurrentHashMap;

public abstract class HandlerBase {
    final static Logger baseLogger = new Logger(HandlerBase.class);
    private final static ConcurrentHashMap<String, HandlerBase> types = new ConcurrentHashMap<>();

    public static <T extends HandlerBase> void getHandlerByType(DiscordInternal discord, JsonNode data) {
        T value;
        String type = data.get("type").asText();

        if (types.containsKey(type)) {
            value = (T) types.get(type);
            discord.getThreadPool().execute(() -> value.handle(discord, data.get("d")));
        } else {
            try {

                Package handlerPackage = HandlerBase.class.getPackage();
                String t1 = handlerPackage.getName() + "." + type;
                Class<T> tClass = (Class<T>) Class.forName(t1);
                value = tClass.getConstructor().newInstance();
                types.put(type, value);
                discord.getThreadPool().execute(() -> value.handle(discord, data.get("d")));
            } catch (ClassNotFoundException e) {
                baseLogger.exception(e, "Failed to dispatch unknown type: " + data.get("t"));
            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                baseLogger.exception(e, "Failed to create instance of handler: "+data.get("t"));
            }
        }
    }

    public abstract void handle(DiscordInternal discord, JsonNode data);
}
