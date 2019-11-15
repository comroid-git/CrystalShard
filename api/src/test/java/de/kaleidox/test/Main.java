package de.kaleidox.test;

import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.api.event.channel.ChannelEvent;
import de.comroid.crystalshard.api.event.multipart.message.MessageSentEvent;
import de.comroid.crystalshard.api.listener.message.MessageSentListener;
import de.comroid.crystalshard.api.listener.model.ListenerAttachable;

public class Main {
    private static final Discord API;

    static {
        API = Discord.builder()
                .setToken(System.getenv("token"))
                .build()
                .join();
    }

    public static void main(String[] args) {
        API.attachListener((MessageSentListener) event -> {
            if (event.getTriggeringMessage().getContent().equals("Hello CrystalShard!"))
                event.getTriggeringChannel()
                        .composeMessage()
                        .addText("Hello "+event.getTriggeringUser().getDiscriminatedName())
                        .send()
                        .join();
        });
    }
}
