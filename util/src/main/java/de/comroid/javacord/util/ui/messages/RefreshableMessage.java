package de.comroid.javacord.util.ui.messages;

import java.io.File;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.Messageable;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.reaction.SingleReactionEvent;

public class RefreshableMessage {
    private final static ConcurrentHashMap<Messageable, RefreshableMessage> selfMap = new ConcurrentHashMap<>();
    private final static String REFRESH_EMOJI = "\uD83D\uDD04";

    private Messageable parent;
    private Supplier<Object> refresher;

    private Message lastMessage = null;

    private RefreshableMessage(Messageable inParent, Supplier<Object> refresher) {
        this.parent = inParent;
        this.refresher = refresher;

        Object item = refresher.get();
        CompletableFuture<Message> sent = null;

        if (item instanceof EmbedBuilder) {
            sent = parent.sendMessage((EmbedBuilder) item);
        } else if (item instanceof String) {
            sent = parent.sendMessage((String) item);
        } else if (item instanceof File) {
            sent = parent.sendMessage((File) item);
        }

        if (sent != null) {
            sent.thenAcceptAsync(msg -> {
                lastMessage = msg;
                msg.addReactionAddListener(this::onRefresh);
                msg.addReactionRemoveListener(this::onRefresh);
                msg.addReaction(REFRESH_EMOJI);
            });
        }
    }

    public void refresh() {
        if (lastMessage != null) {
            Object item = refresher.get();

            if (item instanceof EmbedBuilder) {
                lastMessage.edit((EmbedBuilder) item);
            } else if (item instanceof String) {
                lastMessage.edit((String) item);
            } else if (item instanceof File) {
                resend();
            }
        }
    }

    public void resend() {
        Object item = refresher.get();
        CompletableFuture<Message> sent = null;

        if (lastMessage != null) {
            lastMessage.delete("Outdated");
        }

        if (item instanceof EmbedBuilder) {
            sent = parent.sendMessage((EmbedBuilder) item);
        } else if (item instanceof String) {
            sent = parent.sendMessage((String) item);
        } else if (item instanceof File) {
            sent = parent.sendMessage((File) item);
        }

        if (sent != null) {
            sent.thenAcceptAsync(msg -> {
                lastMessage = msg;
                msg.addReactionAddListener(this::onRefresh);
                msg.addReactionRemoveListener(this::onRefresh);
                msg.addReaction(REFRESH_EMOJI);
            });
        }
    }

    private void onRefresh(SingleReactionEvent event) {
        if (!event.getUser().isYourself()) {
            if (event.getEmoji().asUnicodeEmoji().orElse("").equals(REFRESH_EMOJI)) {
                this.refresh();
            }
        }
    }

    public final static RefreshableMessage get(Messageable forParent, Supplier<Object> defaultRefresher) {
        if (selfMap.containsKey(forParent)) {
            RefreshableMessage val = selfMap.get(forParent);
            val.resend();

            return val;
        } else {
            return selfMap.put(forParent,
                    new RefreshableMessage(
                            forParent,
                            defaultRefresher
                    )
            );
        }
    }

    public final static Optional<RefreshableMessage> get(Messageable forParent) {
        if (selfMap.containsKey(forParent)) {
            RefreshableMessage val = selfMap.get(forParent);
            val.resend();

            return Optional.of(val);
        } else return Optional.empty();
    }
}
