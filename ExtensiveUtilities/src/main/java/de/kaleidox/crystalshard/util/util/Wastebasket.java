package de.kaleidox.crystalshard.util.util;

import de.kaleidox.crystalshard.api.entity.message.Message;
import de.kaleidox.crystalshard.api.entity.server.emoji.Emoji;
import de.kaleidox.crystalshard.api.handling.listener.message.reaction.ReactionAddListener;

import java.util.concurrent.CompletableFuture;

public class Wastebasket {
    // Static members
    // Static membe
    public static void add(Message msg) {
        if (msg.getAuthor()
                .isYourself() && !msg.getPrivateTextChannel()
                .isPresent()) {
            msg.addReaction("🗑");
            msg.attachListener((ReactionAddListener) event -> {
                Emoji emoji = event.getEmoji();

                if (!event.getUser()
                        .isBot()) {
                    emoji.toUnicodeEmoji()
                            .ifPresent(then -> {
                                if (then.equals("🗑")) {
                                    msg.delete();
                                }
                            });
                }
            });
        }
    }

    public static void add(CompletableFuture<Message> messageCompletableFuture) {
        messageCompletableFuture.thenAcceptAsync(Wastebasket::add);
    }
}
