package de.kaleidox.crystalshard.util.discord.util;

import de.kaleidox.crystalshard.main.items.message.MessageReciever;
import de.kaleidox.crystalshard.main.items.message.embed.Embed;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class Warning {
    private static final ConcurrentHashMap<MessageReciever, Long> timeoutMap = new ConcurrentHashMap<>();
    
    public Warning(MessageReciever parent, String text, Embed.Builder baseEmbed, long timeout, TimeUnit timeUnit) {
        if (!timeoutMap.containsKey(parent)) {
            parent.sendMessage(baseEmbed.addField("Warning:", text)
                                       .build());
            timeoutMap.put(parent, System.nanoTime() + timeUnit.toNanos(timeout));
        } else {
            if (timeoutMap.get(parent) < System.nanoTime()) {
                parent.sendMessage(baseEmbed.addField("Warning:", text)
                                           .build());
                timeoutMap.replace(parent, System.nanoTime() + timeUnit.toNanos(timeout));
            }
        }
    }
}
