package org.comroid.test.crystalshard;

import org.comroid.crystalshard.DiscordBot;

public class BotTest {
    public static final DiscordBot bot;

    static {
        bot = DiscordBot.start(System.getenv("Bot-Token"));
    }

    public static void main(String[] args) throws InterruptedException {
        bot.getWebSocket().

                Thread.sleep(99999);
    }
}
