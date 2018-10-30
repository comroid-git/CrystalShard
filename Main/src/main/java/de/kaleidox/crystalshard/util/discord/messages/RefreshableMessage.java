package de.kaleidox.crystalshard.util.discord.messages;

import de.kaleidox.crystalshard.main.handling.event.message.reaction.ReactionEvent;
import de.kaleidox.crystalshard.main.handling.listener.message.reaction.ReactionAddListener;
import de.kaleidox.crystalshard.main.handling.listener.message.reaction.ReactionRemoveListener;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.items.message.MessageReciever;
import de.kaleidox.crystalshard.main.items.message.Sendable;
import de.kaleidox.crystalshard.main.items.server.emoji.Emoji;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class RefreshableMessage {
    private final static ConcurrentHashMap<MessageReciever, RefreshableMessage> selfMap = new ConcurrentHashMap<>();
    private final static String REFRESH_EMOJI = "\uD83D\uDD04";
    private MessageReciever parent;
    private Supplier<Object> refresher;
    private Message lastMessage = null;

    private RefreshableMessage(MessageReciever inParent, Supplier<Object> refresher) {
        this.parent = inParent;
        this.refresher = refresher;

        Object item = refresher.get();
        Sendable sendable = Sendable.of(item);

        CompletableFuture<Message> sent = parent.sendMessage(sendable);

        if (sent != null) {
            sent.thenAcceptAsync(msg -> {
                lastMessage = msg;
                msg.attachListener((ReactionAddListener) this::onRefresh);
                msg.attachListener((ReactionRemoveListener) this::onRefresh);
                msg.addReaction(REFRESH_EMOJI);
            });
        }
    }

    // Static members
    // Static membe
    public final static RefreshableMessage get(MessageReciever forParent, Supplier<Object> defaultRefresher) {
        if (selfMap.containsKey(forParent)) {
            RefreshableMessage val = selfMap.get(forParent);
            val.resend();

            return val;
        } else {
            return selfMap.put(forParent, new RefreshableMessage(forParent, defaultRefresher));
        }
    }

    public final static Optional<RefreshableMessage> get(MessageReciever forParent) {
        if (selfMap.containsKey(forParent)) {
            RefreshableMessage val = selfMap.get(forParent);
            val.resend();

            return Optional.of(val);
        } else return Optional.empty();
    }

    private void onRefresh(ReactionEvent event) {
        if (!event.getUser()
                .isYourself()) {
            Emoji emoji = event.getEmoji();
            if (emoji.getMentionTag()
                    .equals(REFRESH_EMOJI)) {
                this.refresh();
            }
        }
    }

    public void refresh() {
        if (lastMessage != null) {
            Sendable of = Sendable.of(refresher.get());
            lastMessage.edit(of);
        }
    }

    public void resend() {
        Sendable of = Sendable.of(refresher.get());
        CompletableFuture<Message> sent = null;

        if (lastMessage != null) {
            lastMessage.delete("Outdated");
        }

        //noinspection ConstantConditions
        if (sent != null) {
            sent.thenAcceptAsync(msg -> {
                lastMessage = msg;
                msg.attachListener((ReactionAddListener) this::onRefresh);
                msg.attachListener((ReactionRemoveListener) this::onRefresh);
                msg.addReaction(REFRESH_EMOJI);
            });
        }
    }
}
