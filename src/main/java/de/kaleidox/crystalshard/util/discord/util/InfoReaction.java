package de.kaleidox.crystalshard.util.discord.util;

import com.vdurmont.emoji.EmojiParser;
import de.kaleidox.crystalshard.main.handling.listener.message.reaction.ReactionAddListener;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.items.message.embed.Embed;
import de.kaleidox.logging.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

public class InfoReaction {
    public static void add(Message message, String emojiTag, Boolean deleteAfterSend, Embed.Builder infoEmbed) {
        String emoji = EmojiParser.parseToUnicode(emojiTag);
        AtomicReference<Message> sentMessage = new AtomicReference<>();

        message.addReaction(emoji)
                .exceptionally(Logger::get);

        MessageDeleteListener deleteListener = event -> {
            message.removeOwnReactionByEmoji(emoji);
            message.delete();
            message.getMessageAttachableListeners().forEach((key, value) -> message.removeMessageAttachableListener(key));
        };

        ReactionAddListener addListener = event -> {
            if (!event.getUser().isYourself() && event.getEmoji().asUnicodeEmoji().map(emoji::equals).orElse(false)) {
                message.getChannel().sendMessage(infoEmbed)
                        .thenAccept(myMsg -> {
                            sentMessage.set(myMsg);
                            myMsg.addMessageAttachableListener(deleteListener);
                        })
                        .thenAccept(nothing -> {
                            if (deleteAfterSend) {
                                message.delete().exceptionally(Logger::get);
                            }
                        })
                        .exceptionally(Logger::get);
            }
        };

        ReactionRemoveListener removeListener = event -> event.getEmoji().asUnicodeEmoji()
                .filter(emoji::equals)
                .ifPresent(unicodeEmoji -> {
                    if (!event.getUser().isYourself()) {
                        if (event.getUser().equals(message.getUserAuthor().get())) {
                            sentMessage.get().delete().exceptionally(Logger::get);
                        }
                    }
                });

        message.addReactionAddListener(addListener);
        message.addReactionRemoveListener(removeListener);
    }

    public static void add(CompletableFuture<Message> msgFut, String emojiTag, Boolean deleteAfterSend, Embed.Builder infoEmbed) {
        add(msgFut.join(), emojiTag, deleteAfterSend, infoEmbed);
    }

    public static void add(Message message, Embed.Builder infoEmbed) {
        add(message, "ℹ", false, infoEmbed);
    }

    public static void add(CompletableFuture<Message> msgFut, Embed.Builder infoEmbed) {
        add(msgFut.join(), "ℹ", false, infoEmbed);
    }

    public static void remove(Message fromMessage) {
        fromMessage.removeOwnReactionsByEmoji("✅", "❗", "❌", "⛔", "⁉", "\uD83D\uDD1A");
        fromMessage.getMessageAttachableListeners().forEach((key, value) -> fromMessage.removeMessageAttachableListener(key));
    }
}
