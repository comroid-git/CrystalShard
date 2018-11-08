package de.kaleidox.crystalshard.util.embeds;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.kaleidox.crystalshard.internal.InternalDelegate;
import de.kaleidox.crystalshard.internal.items.message.embed.EmbedBuilderInternal;
import de.kaleidox.crystalshard.internal.items.message.embed.EmbedDraftInternal;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.items.message.embed.Embed;
import de.kaleidox.crystalshard.main.items.message.embed.EmbedDraft;
import de.kaleidox.util.helpers.JsonHelper;
import de.kaleidox.util.tunnel.Tunnelable;
import java.awt.Color;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;

public class PagedEmbed extends EmbedBuilderInternal implements Embed.Builder, Tunnelable<Message> {
    public final static int MAX_CHARS_PER_PAGE = 4500;
    public final static int MAX_FIELDS_PER_PAGE = 8;
    public final static int MAX_INSTANCES_PER_CHANNEL = 2;
    public final static String PREV_PAGE_EMOJI = "⬅";
    public final static String NEXT_PAGE_EMOJI = "➡";
    public final static String DELETE_EMOJI = "🗑";
    private final static List<Built> built = new ArrayList<>();
    private Hashtable<Integer, List<EmbedDraft.Field>> pages;
    private int page;

    public PagedEmbed() {
        super();
        ignoreFieldCount = true;
        page = 0;
        pages = new Hashtable<>();
    }

    @Override
    public EmbedDraft build() {
        partitionFields();
        return new Built(
                this,
                title,
                description,
                url,
                timestamp,
                color,
                footer,
                image,
                thumbnail,
                author,
                new ArrayList<>() // doesnt matter if we give a full list or not, as the toJsonNode method is overridden
        );
    }

    @Override
    public Embed.Builder addField(EmbedDraft.Field field) {
        super.addField(field);
        return this;
    }

    @Override
    public Embed.Builder addField(String title, String text, boolean inline) {
        EmbedDraft.Field field = InternalDelegate.newInstance(EmbedDraft.Field.class, title, text, inline);
        addField(field);
        return this;
    }

    @Override
    public Embed.Builder addField(String title, String text) {
        addField(title, text, false);
        return this;
    }

    @Override
    public void handleTunnel(Message message) {
        // TODO: 08.11.2018 Add listeners ETC
    }

    private void partitionFields() {
        Hashtable<Integer, List<EmbedDraft.Field>> prevMap = new Hashtable<>();



        pages.putAll(prevMap);
    }

    private class Built extends EmbedDraftInternal implements PagedEmbedBuilt {
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

    public class Tunnel implements PagedEmbedBuilt.Tunnel {
        @Override
        public void acceptBase(PagedEmbedBuilt base, CompletableFuture<Message> futureResolver) {
            final PagedEmbed pagedEmbed = PagedEmbed.built.stream()
                    .filter(b -> b == base)
                    .findAny()
                    .orElseThrow(NoSuchElementException::new)
                    .root;
            futureResolver.thenAcceptAsync(pagedEmbed::handleTunnel);
        }
    }
}
