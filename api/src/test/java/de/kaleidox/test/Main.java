package de.kaleidox.test;

import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.entity.channel.Channel;
import de.kaleidox.crystalshard.api.event.channel.ChannelEvent;
import de.kaleidox.crystalshard.api.event.message.MessageSentEvent;
import de.kaleidox.crystalshard.api.listener.model.ListenerAttachable;
import de.kaleidox.crystalshard.api.model.message.TextDecoration;

public class Main {
    private static final Discord API;

    static {
        API = Discord.builder()
                .setToken(System.getenv("token"))
                .build()
                .join();
    }

    public static void main(String[] args) {
        API.getCacheManager()
                .getChannelByID(487700636280946688L)
                .flatMap(Channel::asGuildTextChannel)
                .ifPresent(gtc -> gtc.listenInStream(MessageSentEvent.class)
                        .map(ListenerAttachable.EventPair::getEvent)
                        .filter(event -> event.getMessage()
                                .getContent()
                                .startsWith("!bot "))
                        .map(ChannelEvent::getChannel)
                        .forEach(chl -> chl.composeMessage()
                                .addText("hello world!", TextDecoration.QUOTE)
                                .send()));
    }
}
