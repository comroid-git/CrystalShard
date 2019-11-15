package de.comroid.crystalshard.util.ui.reactions;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.api.entity.channel.Channel;
import de.comroid.crystalshard.api.entity.emoji.Emoji;
import de.comroid.crystalshard.api.entity.message.Message;
import de.comroid.crystalshard.api.event.multipart.message.reaction.ReactionEvent;
import de.comroid.crystalshard.api.listener.message.reaction.ReactionAddListener;
import de.comroid.crystalshard.api.listener.message.reaction.ReactionRemoveListener;
import de.comroid.crystalshard.api.model.message.embed.Embed;

import com.google.common.flogger.FluentLogger;

import static de.comroid.crystalshard.adapter.Adapter.exceptionLogger;

public class InfoReaction {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    private final Discord api;
    private final long message;
    private final long channel;
    private final Emoji emoji;
    private final String text;
    private final long time;
    private final TimeUnit unit;
    private final Supplier<Embed> embedBaseSupplier;
    private final AtomicLong sentMessage;

    public InfoReaction(
            Message message,
            Emoji emoji,
            String text,
            long deleteTime,
            TimeUnit deleteUnit,
            Supplier<Embed> embedBaseSupplier
    ) {
        this.api = message.getAPI();
        this.message = message.getID();
        this.channel = message.getChannel().getID();
        this.emoji = emoji;
        this.text = text;
        this.time = deleteTime;
        this.unit = deleteUnit;
        this.embedBaseSupplier = embedBaseSupplier;

        sentMessage = new AtomicLong(-1);
    }

    public void build() {
        Message.request(api, channel, message)
                .thenAccept(message -> {
                    message.addReaction(emoji);
                    message.attachListener((ReactionAddListener) this::handleReaction);
                    message.attachListener((ReactionRemoveListener) this::handleReaction);
                })
                .exceptionally(exceptionLogger());
    }

    private synchronized void handleReaction(ReactionEvent event) {
        if (event.getTriggeringUser().isYourself()) return;
        if (!event.getEmoji().asUnicodeEmoji().map(emoji::equals).orElse(false)) return;

        if (sentMessage.get() == -1) {
            api.getChannelByID(channel)
                    .flatMap(Channel::asTextChannel)
                    .ifPresent(tc -> tc.composeMessage()
                            .setEmbed(embedBaseSupplier.get()
                            .setDescription(text)
                            .setFooter("This message will self-delete in " + time + " " + unit.name().toLowerCase()))
                            .send()
                            .thenAccept(msg -> {
                                sentMessage.set(msg.getID());
                                msg.getAPI()
                                        .getCommonThreadPool()
                                        .schedule(() -> {
                                            msg.delete();
                                            sentMessage.set(-1);
                                        }, time, unit);
                            })
                            .exceptionally(exceptionLogger()));
        } else {
            Message.request(api, channel, sentMessage.get())
                    .thenAccept(Message::delete)
                    .thenRun(() -> sentMessage.set(-1))
                    .exceptionally(exceptionLogger());
        }
    }

    public static class MessageTypeEmoji {
        public static final Emoji WARNING = Emoji.unicode("⚠");
    }
}
