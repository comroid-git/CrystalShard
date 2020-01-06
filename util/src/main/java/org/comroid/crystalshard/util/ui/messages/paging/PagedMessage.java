package org.comroid.crystalshard.util.ui.messages.paging;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import org.comroid.crystalshard.api.entity.emoji.Emoji;
import org.comroid.crystalshard.api.entity.message.Message;
import org.comroid.crystalshard.api.event.multipart.message.reaction.ReactionEvent;
import org.comroid.crystalshard.api.listener.message.reaction.ReactionAddListener;
import org.comroid.crystalshard.api.listener.message.reaction.ReactionRemoveListener;
import org.comroid.crystalshard.api.model.message.Messageable;

import static org.comroid.crystalshard.adapter.Adapter.exceptionLogger;

public class PagedMessage {
    private final static ConcurrentHashMap<Messageable, PagedMessage> selfMap = new ConcurrentHashMap<>();
    private final static Emoji PREV_PAGE_EMOJI = Emoji.unicode("⬅");
    private final static Emoji NEXT_PAGE_EMOJI = Emoji.unicode("➡");
    private final static int SUITABLE_MAX_LENGTH = 1700;

    private Messageable parent;
    private Supplier<String> head, body;

    private Message lastMessage = null;
    private List<String> pages = new ArrayList<>();
    private int page;

    private PagedMessage(Messageable inParent, Supplier<String> head, Supplier<String> body) {
        this.parent = inParent;
        this.head = head;
        this.body = body;

        this.page = 0;

        resend();
    }

    public void refresh() {
        page = 0;

        refreshPage();
    }

    public void refreshPage() {
        refreshPages();

        if (lastMessage != null) {
            lastMessage.editor()
                    .setText(getPageContent())
                    .edit();
        }
    }

    public void resend() {
        refreshPages();

        if (lastMessage != null) {
            lastMessage.delete();
        }

        parent.composeMessage()
                .setText(getPageContent())
                .send()
                .thenAcceptAsync(msg -> {
            lastMessage = msg;
                    msg.attachListener((ReactionAddListener) this::onPageClick);
                    msg.attachListener((ReactionRemoveListener) this::onPageClick);
            msg.addReaction(PREV_PAGE_EMOJI);
            msg.addReaction(NEXT_PAGE_EMOJI);
                }).exceptionally(exceptionLogger());
    }

    private void onPageClick(ReactionEvent event) {
        if (!event.getTriggeringUser().isYourself()) {
            Emoji emoji = event.getEmoji().asUnicodeEmoji().orElse(null);
            
            if (PREV_PAGE_EMOJI.equals(emoji)) {
                if (page > 0)
                    page--;

                this.refreshPage();
            } else if (NEXT_PAGE_EMOJI.equals(emoji)) {
                if (page < pages.size() - 1)
                    page++;

                this.refreshPage();
            }
        }
    }

    private String getPageContent() {
        return new StringBuilder()
                .append(pages.get(page))
                .append("\n\n")
                .append("`Page ")
                .append(page + 1)
                .append(" of ")
                .append(pages.size())
                .append(" | ")
                .append("Last Refresh: ")
                .append(new SimpleDateFormat("HH:mm:ss").format(new Timestamp(System.currentTimeMillis())))
                .append(" [")
                .append(Calendar.getInstance().getTimeZone().getDisplayName())
                .append("]`")
                .toString();
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

                if (i == bodyLines.size() - 1 || pageBuilder.length() + bodyLines.get(i + 1).length() >= SUITABLE_MAX_LENGTH) {
                    pages.add(pageBuilder.toString());
                    pageBuilder = new StringBuilder(completeHead);
                }
            }
        }
    }

    public static PagedMessage get(Messageable forParent, Supplier<String> defaultHead, Supplier<String> defaultBody) {
        if (selfMap.containsKey(forParent)) {
            PagedMessage val = selfMap.get(forParent);
            val.resend();

            return val;
        } else {
            return selfMap.put(forParent,
                    new PagedMessage(
                            forParent,
                            defaultHead,
                            defaultBody
                    )
            );
        }
    }

    public static Optional<PagedMessage> get(Messageable forParent) {
        if (selfMap.containsKey(forParent)) {
            PagedMessage val = selfMap.get(forParent);
            val.resend();

            return Optional.of(val);
        } else return Optional.empty();
    }
}
