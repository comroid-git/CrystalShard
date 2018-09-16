package de.kaleidox.crystalshard.util.discord.util;

import de.kaleidox.crystalshard.main.handling.listener.message.generic.MessageDeleteListener;
import de.kaleidox.crystalshard.main.handling.listener.message.reaction.ReactionAddListener;
import de.kaleidox.crystalshard.main.handling.listener.message.reaction.ReactionRemoveListener;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.items.message.embed.Embed;
import de.kaleidox.crystalshard.main.items.server.emoji.Emoji;
import de.kaleidox.crystalshard.main.items.server.emoji.UnicodeEmoji;
import de.kaleidox.logging.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

public class InfoReaction {
    public static void add(Message message, Emoji emoji, Boolean deleteAfterSend, Embed.Builder infoEmbed) {
        AtomicReference<Message> sentMessage = new AtomicReference<>();

        message.addReaction(emoji)
                .exceptionally(Logger::get);

        MessageDeleteListener deleteListener = event -> {
            message.removeOwnReactionsByEmoji(emoji);
            message.delete();
        };

        ReactionAddListener addListener = event -> {
            if (!event.getUser().isYourself() &&
                    event.getEmoji().toUnicodeEmoji().map(emoji::equals).orElse(false)) {
                message.getChannel().sendMessage(infoEmbed.build())
                        .thenAccept(myMsg -> {
                            sentMessage.set(myMsg);
                            myMsg.attachListener(deleteListener);
                        })
                        .thenAccept(nothing -> {
                            if (deleteAfterSend) {
                                message.delete().exceptionally(Logger::get);
                            }
                        })
                        .exceptionally(Logger::get);
            }
        };

        ReactionRemoveListener removeListener = event -> event.getEmoji()
                .toUnicodeEmoji()
                .filter(emoji::equals)
                .ifPresent(unicodeEmoji -> {
                    if (!event.getUser().isYourself()) {
                        //noinspection OptionalGetWithoutIsPresent
                        if (event.getUser().equals(message.getAuthorAsUser().get())) {
                            sentMessage.get().delete().exceptionally(Logger::get);
                        }
                    }
                });

        message.attachReactionAddListener(addListener);
        message.attachReactionRemoveListener(removeListener);
    }

    public static void add(CompletableFuture<Message> msgFut, Emoji emoji, Boolean deleteAfterSend, Embed.Builder infoEmbed) {
        add(msgFut.join(), emoji, deleteAfterSend, infoEmbed);
    }

    public static void add(Message message, Embed.Builder infoEmbed) {
        add(message, UnicodeEmoji.of(message.getDiscord(), "ℹ"), false, infoEmbed);
    }

    public static void add(CompletableFuture<Message> msgFut, Embed.Builder infoEmbed) {
        Message msg = msgFut.join();
        add(msg, UnicodeEmoji.of(msg.getDiscord(), "ℹ"), false, infoEmbed);
    }
}
