package de.kaleidox.crystalshard.util.discord.messages;

import de.kaleidox.crystalshard.logging.Logger;
import de.kaleidox.crystalshard.main.handling.event.message.reaction.ReactionEvent;
import de.kaleidox.crystalshard.main.handling.listener.message.generic.MessageDeleteListener;
import de.kaleidox.crystalshard.main.handling.listener.message.reaction.ReactionAddListener;
import de.kaleidox.crystalshard.main.handling.listener.message.reaction.ReactionRemoveListener;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.items.message.MessageReciever;
import de.kaleidox.crystalshard.main.items.message.embed.Embed;
import de.kaleidox.crystalshard.main.items.message.embed.EmbedDraft;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class PagedEmbed {
    // Static Fields
    public final static int MAX_CHARS_PER_PAGE = 4500;
    public final static int MAX_FIELDS_PER_PAGE = 8;
    public final static int MAX_INSTANCES_PER_CHANNEL = 2;
    public final static String PREV_PAGE_EMOJI = "⬅";
    public final static String NEXT_PAGE_EMOJI = "➡";
    public final static String DELETE_EMOJI = "🗑";
    public final static ConcurrentHashMap<Long, ConcurrentLinkedQueue<PagedEmbed>> instances = new ConcurrentHashMap<>();
    private final MessageReciever messageable;
    private final Supplier<Embed.Builder> embedsupplier;
    private ConcurrentHashMap<Integer, List<EmbedDraft.Field>> pages = new ConcurrentHashMap<>();
    private List<EmbedDraft.Field> fields = new ArrayList<>();
    private int page;
    private AtomicReference<Message> sentMessage = new AtomicReference<>();

    /**
     * Creates a new PagedEmbed object.
     *
     * @param messageable   The Messageable in which the embed should be sent.
     * @param embedsupplier A Predicate to supply a new, clean EmbedBuilder, that the sent embed should be based on.
     */
    public PagedEmbed(MessageReciever messageable, Supplier<Embed.Builder> embedsupplier) {
        instances.putIfAbsent(messageable.getId(), new ConcurrentLinkedQueue<>());
        if (instances.get(messageable.getId())
                .size() >= MAX_INSTANCES_PER_CHANNEL) {
            //noinspection ConstantConditions
            instances.get(messageable.getId())
                    .poll()
                    .destroy();
        }

        this.messageable = messageable;
        this.embedsupplier = embedsupplier;
    }

    private void destroy() {
        Message msg = sentMessage.get();
        msg.detachAllListeners();
        msg.delete("Deleted");
    }

    /**
     * Adds a new non-inline field to the paged embed.
     *
     * @param title The title of the field.
     * @param text  The text of the field.
     * @return The new, modified PagedEmbed object.
     */
    public PagedEmbed addField(String title, String text) {
        return addField(title, text, false);
    }

    /**
     * Adds a new field to the pages embed.
     *
     * @param title  The title of the field.
     * @param text   The text of the field.
     * @param inline If the field should be inline.
     * @return The new, modified PageEmbed object.
     */
    public PagedEmbed addField(String title, String text, boolean inline) {
        fields.add(EmbedDraft.Field.BUILD(title, text, inline));

        return this;
    }

    /**
     * Builds and sends the PagedEmbed.
     *
     * @return A {@code CompletableFuture} that will contain the sent message.
     */
    public CompletableFuture<Message> build() {
        page = 1;
        refreshPages();

        CompletableFuture<Message> future = messageable.sendMessage(embedsupplier.get()
                .build());

        future.thenAcceptAsync(message -> {
            sentMessage.set(message);
            if (pages.size() != 1) {
                message.addReaction(PREV_PAGE_EMOJI);
                message.addReaction(NEXT_PAGE_EMOJI);
                message.attachListener((ReactionAddListener) this::onReactionClick);
                message.attachListener((ReactionRemoveListener) this::onReactionClick);
            }

            message.attachListener((MessageDeleteListener) delete -> {
                messageable.getDiscord()
                        .getThreadPool()
                        .getScheduler()
                        .schedule(() -> {
                            sentMessage.get()
                                    .removeAllReactions();
                            sentMessage.get()
                                    .detachAllListeners();
                        }, 3, TimeUnit.HOURS);
            });
        })
                .exceptionally(Logger::handle);

        return future;
    }

    /**
     * Firstly, clears all current pages from the embed.
     * <p>
     * Secondly, re-creates the pages in the {@code pages} map from all the stored fields.
     * <p>
     * Thirdly, re-creates the embed for the displayed message.
     */
    private void refreshPages() {
        int fieldCount = 0, pageChars = 0, totalChars = 0, thisPage = 1;
        pages.clear();

        for (EmbedDraft.Field field : fields) {
            pages.putIfAbsent(thisPage, new ArrayList<>());

            if (fieldCount <= MAX_FIELDS_PER_PAGE && pageChars <= Embed.Boundaries.FIELD_TEXT_LENGTH * fieldCount && totalChars < MAX_CHARS_PER_PAGE) {
                pages.get(thisPage)
                        .add(field);

                fieldCount++;
                pageChars = pageChars + field.getTotalCharCount();
                totalChars = totalChars + field.getTotalCharCount();
            } else {
                thisPage++;
                pages.putIfAbsent(thisPage, new ArrayList<>());

                pages.get(thisPage)
                        .add(field);

                fieldCount = 1;
                pageChars = field.getTotalCharCount();
                totalChars = field.getTotalCharCount();
            }
        }

        // Refresh the embed to the current page
        Embed.Builder embed = embedsupplier.get();

        pages.get(page)
                .forEach(field -> {
                    embed.addField(field.getTitle(), field.getText(), field.isInline());
                });
        embed.setFooter("Page " + page + " of " + pages.size());

        // Edit sent message
        if (sentMessage.get() != null) {
            sentMessage.get()
                    .edit(embed.build());
        }
    }

    private void onReactionClick(ReactionEvent event) {
        event.getEmoji()
                .toUnicodeEmoji()
                .ifPresent(emoji -> {
                    if (!event.getUser()
                            .isYourself()) {
                        switch (emoji.getMentionTag()) {
                            case PREV_PAGE_EMOJI:
                                if (page > 1) page--;
                                else if (page == 1) page = pages.size();

                                this.refreshPages();
                                break;
                            case NEXT_PAGE_EMOJI:
                                if (page < pages.size()) page++;
                                else if (page == pages.size()) page = 1;

                                this.refreshPages();
                                break;
                            case DELETE_EMOJI:
                                destroy();
                                break;
                            default:
                                break;
                        }
                    }
                });
    }

    public Embed.Builder getRawEmbed() {
        return embedsupplier.get();
    }

    public Supplier<Embed.Builder> getEmbedsupplier() {
        return embedsupplier;
    }
}