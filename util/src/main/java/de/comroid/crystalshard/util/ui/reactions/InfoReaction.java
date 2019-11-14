package de.comroid.crystalshard.util.ui.reactions;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.reaction.SingleReactionEvent;
import org.javacord.api.util.logging.ExceptionLogger;
import org.javacord.core.util.logging.LoggerUtil;

public class InfoReaction {
    private static final Logger logger = LoggerUtil.getLogger(InfoReaction.class);

    private final DiscordApi api;
    private final long message;
    private final long channel;
    private final String emoji;
    private final String text;
    private final long time;
    private final TimeUnit unit;
    private final Supplier<EmbedBuilder> embedBaseSupplier;
    private final AtomicLong sentMessage;

    public InfoReaction(
            Message message,
            String emoji,
            String text,
            long deleteTime,
            TimeUnit deleteUnit,
            Supplier<EmbedBuilder> embedBaseSupplier
    ) {
        this.api = message.getApi();
        this.message = message.getId();
        this.channel = message.getChannel().getId();
        this.emoji = emoji;
        this.text = text;
        this.time = deleteTime;
        this.unit = deleteUnit;
        this.embedBaseSupplier = embedBaseSupplier;

        sentMessage = new AtomicLong(-1);
    }

    public void build() {
        api.getChannelById(channel)
                .flatMap(Channel::asTextChannel)
                .map(channel -> api.getMessageById(message, channel))
                .map(future -> {
                    try {
                        return future.join();
                    } catch (Exception e) {
                        logger.catching(e);
                        return null;
                    }
                })
                .ifPresent(message -> {
                    message.addReaction(emoji);
                    message.addReactionAddListener(this::handleReaction);
                    message.addReactionRemoveListener(this::handleReaction);
                });
    }

    private synchronized void handleReaction(SingleReactionEvent event) {
        if (event.getUser().isYourself()) return;
        if (!event.getEmoji().asUnicodeEmoji().map(emoji::equals).orElse(false)) return;

        if (sentMessage.get() == -1) {
            api.getChannelById(channel)
                    .flatMap(Channel::asTextChannel)
                    .ifPresent(tc -> tc.sendMessage(embedBaseSupplier.get()
                            .setDescription(text)
                            .setFooter("This message will self-delete in " + time + " " + unit.name().toLowerCase()))
                            .thenAccept(msg -> {
                                sentMessage.set(msg.getId());
                                msg.getApi()
                                        .getThreadPool()
                                        .getScheduler()
                                        .schedule(() -> {
                                            msg.delete();
                                            sentMessage.set(-1);
                                        }, time, unit);
                            })
                            .exceptionally(ExceptionLogger.get()));
        } else {
            api.getChannelById(channel)
                    .flatMap(Channel::asTextChannel)
                    .map(tc -> api.getMessageById(sentMessage.get(), tc))
                    .ifPresent(fut -> {
                        fut.thenAccept(Message::delete);
                        sentMessage.set(-1);
                    });
        }
    }

    public class MessageTypeEmoji {
        public static final String WARNING = "⚠";
    }
}
