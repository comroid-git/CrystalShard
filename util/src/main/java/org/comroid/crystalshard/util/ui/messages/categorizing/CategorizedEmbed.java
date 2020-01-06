package org.comroid.crystalshard.util.ui.messages.categorizing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.comroid.crystalshard.api.entity.emoji.Emoji;
import org.comroid.crystalshard.api.entity.message.Message;
import org.comroid.crystalshard.api.event.multipart.message.reaction.ReactionAddEvent;
import org.comroid.crystalshard.api.event.multipart.message.reaction.ReactionEvent;
import org.comroid.crystalshard.api.event.multipart.message.reaction.ReactionRemoveEvent;
import org.comroid.crystalshard.api.listener.message.reaction.ReactionAddListener;
import org.comroid.crystalshard.api.listener.message.reaction.ReactionRemoveListener;
import org.comroid.crystalshard.api.model.message.Messageable;
import org.comroid.crystalshard.api.model.message.embed.Embed;
import org.comroid.crystalshard.util.ui.embed.DefaultEmbedFactory;

public class CategorizedEmbed {
    public static final Emoji BACK_TO_MENU_EMOJI = Emoji.unicode("◀");

    private final Messageable messageable;
    private final Supplier<Embed> baseEmbedSupplier;
    private final AtomicReference<Message> sentMessage;
    private final List<EmbedCategory> categories;
    private final Listener listener;

    private int state;

    public CategorizedEmbed(Messageable messageable) {
        this(messageable, DefaultEmbedFactory.INSTANCE);
    }

    public CategorizedEmbed(Messageable messageable, Supplier<Embed> baseEmbedSupplier) {
        this.messageable = messageable;
        this.baseEmbedSupplier = baseEmbedSupplier;

        sentMessage = new AtomicReference<>();
        categories = new ArrayList<>();
        listener = new Listener();

        state = -1;
    }

    public EmbedCategory addCategory(String categoryName, String categoryDescription) {
        EmbedCategory category = new EmbedCategory(categoryName, categoryDescription);
        categories.add(category);
        return category;
    }

    public List<EmbedCategory> getCategories() {
        return Collections.unmodifiableList(categories);
    }

    public boolean removeIf(Predicate<EmbedCategory> filter) {
        return categories.removeIf(filter);
    }

    public CompletableFuture<Message> build() {
        return messageable.composeMessage()
                .setEmbed(getMenuEmbed())
                .send()
                .thenApply(msg -> {
                    sentMessage.set(msg);
                    msg.attachListener(listener);
                    getCategoryReactions().forEach(msg::addReaction);
                    return msg;
                });
    }

    private Embed getCategoryEmbed(int no) {
        EmbedCategory category = categories.get(no);
        Embed embed = baseEmbedSupplier.get();

        embed.setAuthor(category.getName())
                .setDescription(category.getDescription())
                .setFooter("Click the " + BACK_TO_MENU_EMOJI + " reaction to go back to the menu!");
        for (Embed.Field field : category.getFields())
            embed.addField(field.getName(), field.getValue(), field.isInline());

        return embed;
    }

    private Embed getMenuEmbed() {
        if (categories.size() > 20)
            throw new IllegalStateException("Cannot have more than 20 categories!");
        if (categories.size() == 0)
            throw new IllegalStateException("No categories set!");

        Embed embed = baseEmbedSupplier.get();
        List<Emoji> emojis = getCategoryReactions();

        embed.setFooter("Click an emoji to open the corresponding category!");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < emojis.size(); i++) {
            EmbedCategory category = categories.get(i);
            String emoji = emojis.get(i)
                    .getDiscordPrintableString();

            sb.append(emoji)
                    .append(" - ")
                    .append(category.getName())
                    .append("\n");
        }

        return embed.setDescription(sb.toString());
    }

    private List<Emoji> getCategoryReactions() {
        String[] yields = new String[categories.size()];

        //noinspection ConstantConditions,PointlessBooleanExpression TODO: get proper emojis
        if (yields.length < 11 && false) {
            switch (yields.length) {
                case 10:
                    yields[9] = "\uD83D\uDD1F";
                case 9:
                    yields[8] = "9️⃣";
                case 8:
                    yields[7] = "8️⃣";
                case 7:
                    yields[6] = "7️⃣";
                case 6:
                    yields[5] = "6️⃣";
                case 5:
                    yields[4] = "5️⃣";
                case 4:
                    yields[3] = "4️⃣";
                case 3:
                    yields[2] = "3️⃣";
                case 2:
                    yields[1] = "2️⃣";
                case 1:
                    yields[0] = "1️⃣";
            }
        } else if (yields.length < 21) {
            switch (yields.length) {
                case 20:
                    yields[19] = "\uD83C\uDDF9";
                case 19:
                    yields[18] = "\uD83C\uDDF8";
                case 18:
                    yields[17] = "\uD83C\uDDF7";
                case 17:
                    yields[16] = "\uD83C\uDDF6";
                case 16:
                    yields[15] = "\uD83C\uDDF5";
                case 15:
                    yields[14] = "\uD83C\uDDF4";
                case 14:
                    yields[13] = "\uD83C\uDDF3";
                case 13:
                    yields[12] = "\uD83C\uDDF2";
                case 12:
                    yields[11] = "\uD83C\uDDF1";
                case 11:
                    yields[10] = "\uD83C\uDDF0";
                case 10:
                    yields[9] = "\uD83C\uDDEF";
                case 9:
                    yields[8] = "\uD83C\uDDEE";
                case 8:
                    yields[7] = "\uD83C\uDDED";
                case 7:
                    yields[6] = "\uD83C\uDDEC";
                case 6:
                    yields[5] = "\uD83C\uDDEB";
                case 5:
                    yields[4] = "\uD83C\uDDEA";
                case 4:
                    yields[3] = "\uD83C\uDDE9";
                case 3:
                    yields[2] = "\uD83C\uDDE8";
                case 2:
                    yields[1] = "\uD83C\uDDE7";
                case 1:
                    yields[0] = "\uD83C\uDDE6";
            }
        }

        return Stream.of(yields)
                .map(Emoji::unicode)
                .collect(Collectors.toList());
    }

    private class Listener implements ReactionAddListener, ReactionRemoveListener {
        @Override
        public void onReactionAdd(ReactionAddEvent event) {
            handleReaction(event);
        }

        @Override
        public void onReactionRemove(ReactionRemoveEvent event) {
            handleReaction(event);
        }

        private void handleReaction(ReactionEvent event) {
            if (event.getTriggeringUser().isYourself())
                return;

            int newState = -1, i = 0;
            for (Emoji reaction : getCategoryReactions()) {
                if (event.getEmoji().asUnicodeEmoji().map(reaction::equals).orElse(false))
                    newState = i;
                if (event.getEmoji().asUnicodeEmoji().map(BACK_TO_MENU_EMOJI::equals).orElse(false)) {
                    newState = -1;
                    break;
                }
                i++;
            }

            Message msg = sentMessage.get();
            if (state == -1) {
                // in menu
                if (newState == -1) return;

                msg.editor()
                        .setEmbed(getCategoryEmbed(newState))
                        .edit()
                        .thenRun(() -> {
                            msg.removeAllReactions();
                            msg.addReaction(BACK_TO_MENU_EMOJI);
                        });
            } else {
                // in category
                if (newState == -1) {
                    // goto menu
                    msg.editor()
                            .setEmbed(getMenuEmbed())
                            .edit()
                            .thenRun(() -> {
                                msg.removeAllReactions();
                                getCategoryReactions().forEach(msg::addReaction);
                            });
                } else {
                    // goto category
                    msg.editor()
                            .setEmbed(getCategoryEmbed(newState))
                            .edit()
                            .thenRun(() -> {
                                msg.removeAllReactions();
                                msg.addReaction(BACK_TO_MENU_EMOJI);
                            });
                }
            }
            state = newState;
        }
    }
}
