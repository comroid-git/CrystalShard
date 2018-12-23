package de.kaleidox.crystalshard.util.ui.response;

import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.items.message.MessageReciever;
import de.kaleidox.crystalshard.main.items.message.embed.Embed;
import de.kaleidox.crystalshard.main.items.user.User;
import de.kaleidox.crystalshard.util.ui.DialogueBranch;
import de.kaleidox.util.markers.NamedItem;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.function.Supplier;

@SuppressWarnings({"unused", "WeakerAccess", "FieldCanBeLocal", "UnusedReturnValue"})
public abstract class ResponseElement<ResultType> {
    protected final String name;
    protected final MessageReciever parent;
    protected final Supplier<Embed.Builder> embedBaseSupplier;
    protected final Predicate<User> userCanRespond;
    protected final ArrayList<Message> affiliateMessages;
    // Default Settings
    protected long duration = 5;
    protected TimeUnit timeUnit = TimeUnit.MINUTES;
    protected boolean deleteLater = false;
    private DialogueBranch parentBranch;

    public ResponseElement(String name, MessageReciever parent, Supplier<Embed.Builder> embedBaseSupplier, Predicate<User> userCanRespond) {
        this.name = name;
        this.parent = parent;
        this.embedBaseSupplier = (embedBaseSupplier == null ? () -> parent.getDiscord()
                .getUtilities()
                .getDefaultEmbed()
                .getBuilder() : embedBaseSupplier);
        this.userCanRespond = (userCanRespond == null ? user -> true : userCanRespond);

        this.affiliateMessages = new ArrayList<>();
    }

    public ResponseElement<ResultType> setParentBranch(DialogueBranch parentBranch) {
        this.parentBranch = parentBranch;
        return this;
    }

    /**
     * Toggles whether the message should be deleted after a response. Default is "FALSE".
     *
     * @return The instance of the ResponseElement for chaining methods.
     */
    public ResponseElement<ResultType> deleteLater() {
        return deleteLater(!deleteLater);
    }

    /**
     * Sets whether the messages should be deleted after the response.
     *
     * @param bool Whether the message should be deleted.
     * @return The instance of the ResponseElement for chaining methods.
     */
    public ResponseElement<ResultType> deleteLater(boolean bool) {
        this.deleteLater = bool;
        return this;
    }

    /**
     * Builds and Sends the ResponseElement to the Messageable.
     *
     * @param duration The duration that the vote is active for.
     * @param timeUnit The time unit the duration is measured in.
     * @return A Future that will contain the response value. Otherwise contains {@code null}.
     */
    public CompletableFuture<ResultType> build(long duration, TimeUnit timeUnit) {
        setTimeout(duration, timeUnit);
        return CompletableFuture.supplyAsync(build().join()::getItem);
    }

    public abstract CompletableFuture<NamedItem<ResultType>> build();

    /**
     * Sets how long it will take for the ResponseElement to end.
     *
     * @param duration The duration that the vote is active for.
     * @param timeUnit The time unit the duration is measured in.
     * @return The instance of the ResponseElement for chaining methods.
     */
    public ResponseElement<ResultType> setTimeout(long duration, TimeUnit timeUnit) {
        this.duration = duration;
        this.timeUnit = timeUnit;

        return this;
    }

    public String getName() {
        return name;
    }

    // Static membe
    public static Predicate<User> sameUserPredicate(User user) {
        return usr -> usr.equals(user);
    }
}
