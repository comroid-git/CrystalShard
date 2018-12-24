package de.kaleidox.crystalshard.util.messages;

import de.kaleidox.crystalshard.api.entity.message.Message;
import de.kaleidox.crystalshard.api.entity.message.MessageReciever;
import de.kaleidox.crystalshard.api.entity.message.embed.EmbedDraft;
import de.kaleidox.crystalshard.api.entity.server.emoji.Emoji;
import de.kaleidox.crystalshard.api.handling.event.message.reaction.ReactionEvent;
import de.kaleidox.crystalshard.api.handling.listener.message.reaction.ReactionAddListener;
import de.kaleidox.crystalshard.api.handling.listener.message.reaction.ReactionRemoveListener;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class RefreshableMessage {
    private final static ConcurrentHashMap<MessageReciever, RefreshableMessage> selfMap = new ConcurrentHashMap<>();
    private final static String REFRESH_EMOJI = "\uD83D\uDD04";
    private MessageReciever parent;
    private Supplier<EmbedDraft> refresher;
    private Message lastMessage = null;

    private RefreshableMessage(MessageReciever inParent, Supplier<EmbedDraft> refresher) {
        this.parent = inParent;
        this.refresher = refresher;

        Object item = refresher.get();

        CompletableFuture<Message> sent = parent.sendMessage(refresher.get());

        if (sent != null) {
            sent.thenAcceptAsync(msg -> {
                lastMessage = msg;
                msg.attachListener((ReactionAddListener) this::onRefresh);
                msg.attachListener((ReactionRemoveListener) this::onRefresh);
                msg.addReaction(REFRESH_EMOJI);
            });
        }
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
            lastMessage.edit(refresher.get());
        }
    }

    public void resend() {
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

    public static RefreshableMessage get(MessageReciever forParent, Supplier<EmbedDraft> defaultRefresher) {
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
}
