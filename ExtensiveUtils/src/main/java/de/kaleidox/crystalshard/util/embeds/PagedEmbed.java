package de.kaleidox.crystalshard.util.embeds;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.kaleidox.crystalshard.internal.InternalInjector;
import de.kaleidox.crystalshard.internal.items.message.embed.EmbedBuilderInternal;
import de.kaleidox.crystalshard.internal.items.message.embed.EmbedDraftInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.handling.event.message.reaction.ReactionEvent;
import de.kaleidox.crystalshard.main.handling.listener.message.reaction.ReactionAddListener;
import de.kaleidox.crystalshard.main.handling.listener.message.reaction.ReactionRemoveListener;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.items.message.embed.Embed;
import de.kaleidox.crystalshard.main.items.message.embed.EmbedDraft;
import de.kaleidox.util.helpers.JsonHelper;
import java.awt.Color;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class PagedEmbed extends EmbedBuilderInternal implements Embed.Builder {
    public final static long KEEPALIVE_TIME = 4;
    public final static TimeUnit KEEPALIVE_UNIT = TimeUnit.HOURS;
    public final static int MAX_CHARS_PER_PAGE = 4500;
    public final static int MAX_FIELDS_PER_PAGE = 8;
    public final static int MAX_INSTANCES_PER_CHANNEL = 2;
    public final static String PREV_PAGE_EMOJI = "⬅";
    public final static String NEXT_PAGE_EMOJI = "➡";
    public final static String DELETE_EMOJI = "🗑";
    private final static List<Built> built = new ArrayList<>();
    private final Discord discord;
    private Built myBuilt;
    private Hashtable<Integer, List<EmbedDraft.Field>> pages;
    private final AtomicReference<Message> sentMessage;
    private int page;

    public PagedEmbed(Discord discord) {
        super();
        this.discord = discord;
        ignoreFieldCount = true;
        page = 0;
        pages = new Hashtable<>();
        sentMessage = new AtomicReference<>();
    }

    @Override
    public EmbedDraft build() {
        partitionFields();
        myBuilt = new Built(this, title, description, url, timestamp, color, footer, image, thumbnail, author,
                new ArrayList<>() // doesnt matter if we give a full list or not, as the toJsonNode method is overridden
        );
        discord.getTunnelFramework().subscribe(myBuilt, Message.class, this::tunnelHandler);
        return myBuilt;
    }

    @Override
    public Embed.Builder setFooter(EmbedDraft.Footer footer) {
        throw new AbstractMethodError("In the paged embed, the footer is used for telling what page you are on! " +
                "[CANNOT SET FOOTER]");
    }

    @Override
    public Embed.Builder addField(EmbedDraft.Field field) {
        super.addField(field);
        return this;
    }

    @Override
    public Embed.Builder addField(String title, String text, boolean inline) {
        EmbedDraft.Field field = InternalInjector.newInstance(EmbedDraft.Field.class, title, text, inline);
        addField(field);
        return this;
    }

    @Override
    public Embed.Builder addField(String title, String text) {
        addField(title, text, false);
        return this;
    }

    public void tunnelHandler(Message message) {
        sentMessage.set(message);
        message.addReaction(PREV_PAGE_EMOJI, NEXT_PAGE_EMOJI, DELETE_EMOJI);
        message.attachListener((ReactionAddListener) PagedEmbed.this::onReactionClick);
        message.attachListener((ReactionRemoveListener) PagedEmbed.this::onReactionClick);
        message.getDiscord()
                .getScheduler()
                .schedule(() -> sentMessage.get().delete("Keepalive-Time ran out!"),
                        KEEPALIVE_TIME, KEEPALIVE_UNIT);
    }

    public void onReactionClick(ReactionEvent event) {
        if (!event.getUser().isYourself()) {
            switch (event.getEmoji().toDiscordPrintable()) {
                case PREV_PAGE_EMOJI:
                    page--;
                    if (page < 0) page = pages.size()-1;
                    sentMessage.get().edit(myBuilt);
                    break;
                case NEXT_PAGE_EMOJI:
                    page++;
                    if (page >= pages.size()) page = 0;
                    sentMessage.get().edit(myBuilt);
                    break;
                case DELETE_EMOJI:
                    sentMessage.get().delete("Delete-Emoji clicked!");
                    break;
                default:
                    event.getMessage().removeReactionsByEmoji(event.getEmoji());
                    break; // disallow other reactions
            }
        }
    }

    private void partitionFields() {
        Hashtable<Integer, List<EmbedDraft.Field>> tempPages = new Hashtable<>();
        int page = 0;

        for (EmbedDraft.Field field : fields) {
            tempPages.putIfAbsent(page, new ArrayList<>());
            if (tempPages.get(page)
                    .stream()
                    .mapToInt(EmbedDraft.Field::getTotalCharCount)
                    .sum() + field.getTotalCharCount() >= MAX_CHARS_PER_PAGE
                    || tempPages.get(page).size() >= MAX_FIELDS_PER_PAGE) page++;
            tempPages.putIfAbsent(page, new ArrayList<>());
            tempPages.get(page).add(field);
        }

        pages.putAll(tempPages);
    }

    private class Built extends EmbedDraftInternal {
        private final PagedEmbed root;

        public Built(PagedEmbed root,
                     String title,
                     String description,
                     URL url,
                     Instant timestamp,
                     Color color,
                     EmbedDraft.Footer footer,
                     EmbedDraft.Image image,
                     EmbedDraft.Thumbnail thumbnail,
                     EmbedDraft.Author author,
                     ArrayList<EmbedDraft.Field> fields) {
            super(title, description, url, timestamp, color, footer, image, thumbnail, author, fields);
            this.root = root;
            built.add(this);
        }

        /**
         * {@inheritDoc}
         *
         * Only this method is actually differing, because when editing an existing
         * message with an EmbedDraft, this method gets called for the new embed draft.
         */
        @Override
        public ObjectNode toJsonNode(ObjectNode object) {
            ObjectNode obj = super.toJsonNode(object);
            obj.remove("fields");
            if (fields.size() > 0) {
                ArrayNode jsonFields = obj.putArray("fields");
                for (EmbedDraft.Field field : pages.get(page)) {
                    ObjectNode jsonField = jsonFields.addObject();
                    jsonField.set("name", JsonHelper.nodeOf(field.getTitle()));
                    jsonField.set("value", JsonHelper.nodeOf(field.getText()));
                    jsonField.set("inline", JsonHelper.nodeOf(field.isInline()));
                }
            }
            return obj;
        }
    }
}
