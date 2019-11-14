package de.comroid.crystalshard.util.ui.messages.categorizing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.function.Supplier;

import de.comroid.crystalshard.util.ui.embed.DefaultEmbedFactory;

import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.Messageable;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.message.embed.EmbedField;
import org.javacord.api.event.message.reaction.ReactionAddEvent;
import org.javacord.api.event.message.reaction.ReactionRemoveEvent;
import org.javacord.api.event.message.reaction.SingleReactionEvent;
import org.javacord.api.listener.message.reaction.ReactionAddListener;
import org.javacord.api.listener.message.reaction.ReactionRemoveListener;

public class CategorizedEmbed {
    public static final String BACK_TO_MENU_EMOJI = "◀";

    private final Messageable messageable;
    private final Supplier<EmbedBuilder> baseEmbedSupplier;
    private final AtomicReference<Message> sentMessage;
    private final List<EmbedCategory> categories;
    private final Listener listener;

    private int state;

    public CategorizedEmbed(Messageable messageable) {
        this(messageable, DefaultEmbedFactory.INSTANCE);
    }

    public CategorizedEmbed(Messageable messageable, Supplier<EmbedBuilder> baseEmbedSupplier) {
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
        return messageable.sendMessage(getMenuEmbed())
                .thenApply(msg -> {
                    sentMessage.set(msg);
                    msg.addMessageAttachableListener(listener);
                    for (String reaction : getCategoryReactions()) msg.addReaction(reaction);
                    return msg;
                });
    }

    private EmbedBuilder getCategoryEmbed(int no) {
        EmbedCategory category = categories.get(no);
        EmbedBuilder embed = baseEmbedSupplier.get();

        embed.setAuthor(category.getName())
                .setDescription(category.getDescription())
                .setFooter("Click the " + BACK_TO_MENU_EMOJI + " reaction to go back to the menu!");
        for (EmbedField field : category.getFields())
            embed.addField(field.getName(), field.getValue(), field.isInline());

        return embed;
    }

    private EmbedBuilder getMenuEmbed() {
        if (categories.size() > 20)
            throw new IllegalStateException("Cannot have more than 20 categories!");
        if (categories.size() == 0)
            throw new IllegalStateException("No categories set!");

        EmbedBuilder embed = baseEmbedSupplier.get();
        String[] emojis = getCategoryReactions();

        embed.setFooter("Click an emoji to open the corresponding category!");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < emojis.length; i++) {
            EmbedCategory category = categories.get(i);
            String emoji = emojis[i];

            sb.append(emoji)
                    .append(" - ")
                    .append(category.getName())
                    .append("\n");
        }

        return embed.setDescription(sb.toString());
    }

    private String[] getCategoryReactions() {
        String[] yield = new String[categories.size()];

        //noinspection ConstantConditions,PointlessBooleanExpression TODO: get proper emojis
        if (yield.length < 11 && false) {
            switch (yield.length) {
                case 10:
                    yield[9] = "\uD83D\uDD1F";
                case 9:
                    yield[8] = "9️⃣";
                case 8:
                    yield[7] = "8️⃣";
                case 7:
                    yield[6] = "7️⃣";
                case 6:
                    yield[5] = "6️⃣";
                case 5:
                    yield[4] = "5️⃣";
                case 4:
                    yield[3] = "4️⃣";
                case 3:
                    yield[2] = "3️⃣";
                case 2:
                    yield[1] = "2️⃣";
                case 1:
                    yield[0] = "1️⃣";
            }
        } else if (yield.length < 21) {
            switch (yield.length) {
                case 20:
                    yield[19] = "\uD83C\uDDF9";
                case 19:
                    yield[18] = "\uD83C\uDDF8";
                case 18:
                    yield[17] = "\uD83C\uDDF7";
                case 17:
                    yield[16] = "\uD83C\uDDF6";
                case 16:
                    yield[15] = "\uD83C\uDDF5";
                case 15:
                    yield[14] = "\uD83C\uDDF4";
                case 14:
                    yield[13] = "\uD83C\uDDF3";
                case 13:
                    yield[12] = "\uD83C\uDDF2";
                case 12:
                    yield[11] = "\uD83C\uDDF1";
                case 11:
                    yield[10] = "\uD83C\uDDF0";
                case 10:
                    yield[9] = "\uD83C\uDDEF";
                case 9:
                    yield[8] = "\uD83C\uDDEE";
                case 8:
                    yield[7] = "\uD83C\uDDED";
                case 7:
                    yield[6] = "\uD83C\uDDEC";
                case 6:
                    yield[5] = "\uD83C\uDDEB";
                case 5:
                    yield[4] = "\uD83C\uDDEA";
                case 4:
                    yield[3] = "\uD83C\uDDE9";
                case 3:
                    yield[2] = "\uD83C\uDDE8";
                case 2:
                    yield[1] = "\uD83C\uDDE7";
                case 1:
                    yield[0] = "\uD83C\uDDE6";
            }
        }

        return yield;
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

        private void handleReaction(SingleReactionEvent event) {
            if (event.getUser().isYourself()) return;

            int newState = -1, i = 0;
            for (String reaction : getCategoryReactions()) {
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

                msg.edit(getCategoryEmbed(newState))
                        .thenRun(() -> {
                            msg.removeAllReactions();
                            msg.addReaction(BACK_TO_MENU_EMOJI);
                        });
            } else {
                // in category
                if (newState == -1) {
                    // goto menu
                    msg.edit(getMenuEmbed())
                            .thenRun(() -> {
                                msg.removeAllReactions();
                                for (String reaction : getCategoryReactions()) msg.addReaction(reaction);
                            });
                } else {
                    // goto category
                    msg.edit(getCategoryEmbed(newState))
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
