package de.kaleidox.crystalshard.util.messages;

import de.kaleidox.crystalshard.main.handling.event.message.reaction.ReactionEvent;
import de.kaleidox.crystalshard.main.handling.listener.message.reaction.ReactionAddListener;
import de.kaleidox.crystalshard.main.handling.listener.message.reaction.ReactionRemoveListener;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.items.message.MessageReciever;
import de.kaleidox.crystalshard.main.items.server.emoji.Emoji;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class PagedMessage {
    private final static ConcurrentHashMap<MessageReciever, PagedMessage> selfMap = new ConcurrentHashMap<>();
    private final static String PREV_PAGE_EMOJI = "⬅";
    private final static String NEXT_PAGE_EMOJI = "➡";
    private final static int SUITABLE_MAX_LENGTH = 1700;
    private MessageReciever parent;
    private Supplier<String> head, body;
    private Message lastMessage = null;
    private List<String> pages = new ArrayList<>();
    private int page;

    private PagedMessage(MessageReciever inParent, Supplier<String> head, Supplier<String> body) {
        this.parent = inParent;
        this.head = head;
        this.body = body;

        this.page = 0;

        resend();
    }

    // Static members
    // Static membe
    public final static PagedMessage get(MessageReciever forParent, Supplier<String> defaultHead, Supplier<String> defaultBody) {
        if (selfMap.containsKey(forParent)) {
            PagedMessage val = selfMap.get(forParent);
            val.resend();

            return val;
        } else {
            return selfMap.put(forParent, new PagedMessage(forParent, defaultHead, defaultBody));
        }
    }

    public final static Optional<PagedMessage> get(MessageReciever forParent) {
        if (selfMap.containsKey(forParent)) {
            PagedMessage val = selfMap.get(forParent);
            val.resend();

            return Optional.of(val);
        } else return Optional.empty();
    }

    private void onPageClick(ReactionEvent event) {
        if (!event.getUser()
                .isYourself()) {
            Emoji emoji = event.getEmoji();
            switch (emoji.getMentionTag()) {
                case PREV_PAGE_EMOJI:
                    if (page > 0) page--;

                    this.refreshPage();
                    break;
                case NEXT_PAGE_EMOJI:
                    if (page < pages.size() - 1) page++;

                    this.refreshPage();
                    break;
            }
        }
    }

    public void refresh() {
        page = 0;

        refreshPage();
    }

    public void refreshPage() {
        refreshPages();

        if (lastMessage != null) {
            lastMessage.edit(getPageContent());
        }
    }

    public void resend() {
        refreshPages();

        if (lastMessage != null) {
            lastMessage.delete("Outdated");
        }

        parent.sendMessage(getPageContent())
                .thenAcceptAsync(msg -> {
                    lastMessage = msg;
                    msg.attachListener((ReactionAddListener) this::onPageClick);
                    msg.attachListener((ReactionRemoveListener) this::onPageClick);
                    msg.addReaction(PREV_PAGE_EMOJI);
                    msg.addReaction(NEXT_PAGE_EMOJI);
                });
    }

    private String getPageContent() {
        return pages.get(page) + "\n\n" + "`Page " + (page + 1) + " of " + pages.size() + " | " + "Last Refresh: " +
                new SimpleDateFormat("HH:mm:ss").format(new Timestamp(System.currentTimeMillis())) + " [GMT+2]`";
    }

    private void refreshPages() {
        String completeHead = head.get();
        String completeBody = body.get();
        String completeMessage = completeHead + completeBody;
        List<String> bodyLines = Arrays.asList(completeBody.split("\n"));
        StringBuilder pageBuilder;

        pages.clear();

        if (completeMessage.length() < SUITABLE_MAX_LENGTH) {
            pages.add(completeMessage);
        } else {
            pageBuilder = new StringBuilder(completeHead);

            for (int i = 0; i < bodyLines.size(); i++) {
                pageBuilder.append(bodyLines.get(i));
                pageBuilder.append("\n");

                if (i == bodyLines.size() - 1 || pageBuilder.length() + bodyLines.get(i + 1)
                        .length() >= SUITABLE_MAX_LENGTH) {
                    pages.add(pageBuilder.toString());
                    pageBuilder = new StringBuilder(completeHead);
                }
            }
        }
    }
}
