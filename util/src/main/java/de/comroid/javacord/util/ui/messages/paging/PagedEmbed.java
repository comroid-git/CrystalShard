package de.comroid.javacord.util.ui.messages.paging;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import de.comroid.javacord.util.ui.embed.DefaultEmbedFactory;
import de.comroid.javacord.util.ui.embed.EmbedFieldRepresentative;

import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.Messageable;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.reaction.SingleReactionEvent;
import org.javacord.api.util.logging.ExceptionLogger;

public class PagedEmbed {
    private final Messageable messageable;
    private final Supplier<EmbedBuilder> embedsupplier;

    private ConcurrentHashMap<Integer, List<Field>> pages = new ConcurrentHashMap<>();
    private List<Field> fields = new ArrayList<>();
    private int page;
    private AtomicReference<Message> sentMessage = new AtomicReference<>();

    /**
     * Creates a new PagedEmbed object.
     *
     * @param messageable The Messageable in which the embed should be sent.
     */
    public PagedEmbed(Messageable messageable) {
        this(messageable, DefaultEmbedFactory.INSTANCE);
    }

    /**
     * Creates a new PagedEmbed object.
     *
     * @param messageable   The Messageable in which the embed should be sent.
     * @param embedsupplier A Predicate to supply a new, clean EmbedBuilder, that the sent embed should be based on.
     */
    public PagedEmbed(Messageable messageable, Supplier<EmbedBuilder> embedsupplier) {
        this.messageable = messageable;
        this.embedsupplier = embedsupplier;
    }

    /**
     * Adds a new non-inline field to the paged embed.
     *
     * @param title The title of the field.
     * @param text  The text of the field.
     *
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
     *
     * @return The new, modified PageEmbed object.
     */
    public PagedEmbed addField(String title, String text, boolean inline) {
        fields.add(
                new Field(
                        title,
                        text,
                        inline
                )
        );

        return this;
    }

    /**
     * Builds and sends the PagedEmbed.
     *
     * @return A {@code CompletableFuture} that will contain the sent message.
     */
    public CompletableFuture<Message> build() {
        page = 1;

        CompletableFuture<Message> future = messageable.sendMessage(embedsupplier.get())
                .exceptionally(ExceptionLogger.get());

        future.thenAcceptAsync(message -> {
            sentMessage.set(message);
            if (pages.size() != 1) {
                message.addReactionAddListener(this::onReactionClick);
                message.addReactionRemoveListener(this::onReactionClick);
                message.addReaction(Variables.PREV_PAGE_EMOJI);
                message.addReaction(Variables.NEXT_PAGE_EMOJI);
            }

            message.addMessageDeleteListener(delete -> message.getMessageAttachableListeners()
                    .forEach((a, b) -> message.removeMessageAttachableListener(a)))
                    .removeAfter(3, TimeUnit.HOURS)
                    .addRemoveHandler(() -> {
                        sentMessage.get()
                                .removeAllReactions();
                        sentMessage.get()
                                .getMessageAttachableListeners()
                                .forEach((a, b) -> message.removeMessageAttachableListener(a));
                    });
            refreshPages();
        }).exceptionally(ExceptionLogger.get());

        return future;
    }

    public EmbedBuilder getRawEmbed() {
        return embedsupplier.get();
    }

    public Supplier<EmbedBuilder> getEmbedsupplier() {
        return embedsupplier;
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
        int prevSize = pages.size();
        pages.clear();

        for (Field field : fields) {
            pages.putIfAbsent(thisPage, new ArrayList<>());

            if (fieldCount <= Variables.MAX_FIELDS_PER_PAGE &&
                    pageChars <= Variables.FIELD_MAX_CHARS * fieldCount &&
                    totalChars < Variables.MAX_CHARS_PER_PAGE) {
                pages.get(thisPage)
                        .add(field);

                fieldCount++;
                pageChars = pageChars + field.getTotalChars();
                totalChars = totalChars + field.getTotalChars();
            } else {
                thisPage++;
                pages.putIfAbsent(thisPage, new ArrayList<>());

                pages.get(thisPage)
                        .add(field);

                fieldCount = 1;
                pageChars = field.getTotalChars();
                totalChars = field.getTotalChars();
            }
        }

        // Refresh the embed to the current page
        EmbedBuilder embed = embedsupplier.get().removeAllFields();

        pages.get(page)
                .forEach(field -> embed.addField(
                        field.getName(),
                        field.getValue(),
                        field.isInline()
                ));
        embed.setFooter("Page " + page + " of " + pages.size());

        // Edit sent message
        if (sentMessage.get() != null) {
            sentMessage.get().edit(embed);

            if (pages.size() == 1 && prevSize > 1) {
                sentMessage.get().removeOwnReactionByEmoji(Variables.NEXT_PAGE_EMOJI);
                sentMessage.get().removeOwnReactionByEmoji(Variables.PREV_PAGE_EMOJI);
            } else if (pages.size() > 1 && prevSize == 1) {
                sentMessage.get().addReaction(Variables.PREV_PAGE_EMOJI);
                sentMessage.get().addReaction(Variables.NEXT_PAGE_EMOJI);
            }
        }
    }

    private void onReactionClick(SingleReactionEvent event) {
        event.getEmoji().asUnicodeEmoji().ifPresent(emoji -> {
            if (!event.getUser().isYourself()) {
                if (Variables.PREV_PAGE_EMOJI.equals(emoji)) {
                    if (page > 1)
                        page--;
                    else if (page == 1)
                        page = pages.size();

                    this.refreshPages();
                } else if (Variables.NEXT_PAGE_EMOJI.equals(emoji)) {
                    if (page < pages.size())
                        page++;
                    else if (page == pages.size())
                        page = 1;

                    this.refreshPages();
                }
            }
        });
    }

    /**
     * This subclass represents an embed field for the PagedEmbed.
     */
    class Field extends EmbedFieldRepresentative {
        public Field(String title, String text, boolean inline) {
            super(title, text, inline);
        }

        /**
         * Returns the total characters of the field.
         *
         * @return The total characters of the field.
         */
        int getTotalChars() {
            return name.length() + value.length();
        }
    }

    public static class Variables {
        public static int FIELD_MAX_CHARS = 1024;
        public static int MAX_CHARS_PER_PAGE = 4500;
        public static int MAX_FIELDS_PER_PAGE = 8;
        public static String PREV_PAGE_EMOJI = "⬅";
        public static String NEXT_PAGE_EMOJI = "➡";

    }
}