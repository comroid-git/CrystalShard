package de.kaleidox.crystalshard.util.discord.ui;

import de.kaleidox.crystalshard.logging.Logger;
import de.kaleidox.crystalshard.util.discord.ui.response.ResponseElement;
import de.kaleidox.crystalshard.util.objects.markers.NamedItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

/**
 * This class represents a possible branch in a Discord Dialogue. Each branch except the root branch results from a previous branch; and is the product of a
 * specified Option.
 * <p>
 * A Branch consists of a {@link ResponseElement} and a List of Options. Those Options require a {@link Predicate} to test the result of the Branches {@link
 * ResponseElement}, and a sub-branch that will start if the {@link Predicate} is turning out true.
 *
 * @param <A> The type of the object that is requested.
 */
@SuppressWarnings({"FieldCanBeLocal", "WeakerAccess", "unused"})
public class DialogueBranch<A> extends Dialogue {
    final         ResponseElement<A> questionElement;
    final         ArrayList<Option>  options;
    private final Dialogue           previousBranch;
    
    /**
     * Creates a new DialogueBranch without a parent branch.
     *
     * @param questionElement The question element of this branch.
     */
    public DialogueBranch(ResponseElement<A> questionElement) {
        this(null, questionElement);
    }
    
    /**
     * Creates a new DialogueBranch with a parent branch.
     *
     * @param previousBranch  The parent Branch that this branch results from.
     * @param questionElement The question element of this branch.
     */
    public DialogueBranch(Dialogue previousBranch, ResponseElement<A> questionElement) {
        this.previousBranch = previousBranch;
        this.questionElement = questionElement;
        
        this.options = new ArrayList<>();
    }
    
    /**
     * Adds a new handling possibility to the current branch.
     *
     * @param tester          A Predicate to test the result of the question element.
     * @param followingBranch The branch that follows if the question element is tested true.
     * @param <B>             The type of the next branch.
     * @return The instance for chaining methods.
     */
    public <B> DialogueBranch<A> addOption(Predicate<A> tester, DialogueBranch<B> followingBranch) {
        return addOption(new Option<>(tester, this, followingBranch));
    }
    
    /**
     * Adds a new handling possibility to the current branch.
     *
     * @param option The pre-built {@link Option} to add.
     * @param <B>    The type of the next branch.
     * @return The instance for chaining methods.
     */
    public <B> DialogueBranch<A> addOption(Option<B> option) {
        options.add(option);
        
        return this;
    }
    
    @SuppressWarnings("unchecked")
    protected void start(List<NamedItem> collectedItems) throws NullPointerException {
        questionElement.setParentBranch(this).build().thenAcceptAsync(response -> options.stream()
                .filter(option -> ((Predicate<A>) option.tester).test(response.getItem()))
                .map(Option::getGoToBranch)
                .forEachOrdered(branch -> {
                    collectedItems.add(response);
                    if (branch.getClass() == DialogueEndpoint.class) {
                        branch.runEndpoint(collectedItems);
                    } else if (branch.getClass() == DialoguePassthrough.class) {
                        branch.runPassthrough(collectedItems);
                        branch.start(new ArrayList<>());
                    } else {
                        branch.start(collectedItems);
                    }
                })).exceptionally(Logger::get);
    }
    
    protected CompletableFuture<Void> runEndpoint(List<NamedItem> collectedItems) {
        throw new AbstractMethodError("Abstract method; used by DialogueEndpoint.");
    }
    
    protected CompletableFuture<Void> runPassthrough(List<NamedItem> collectedItems) {
        throw new AbstractMethodError("Abstract method; used by DialoguePassthrough.");
    }
    
    /**
     * This subclass represents an option of the result.
     *
     * @param <B> The type of the following branch.
     */
    public class Option<B> {
        private final Predicate<A>      tester;
        private final DialogueBranch<A> parentBranch;
        private final DialogueBranch<B> goToBranch;
        
        /**
         * Creates a new option.
         *
         * @param tester       A Predicate to test the result of the question element.
         * @param parentBranch The parent branch to the option.
         * @param goToBranch   The branch that will follow.
         */
        public Option(Predicate<A> tester, DialogueBranch<A> parentBranch, DialogueBranch<B> goToBranch) {
            this.tester = tester;
            this.parentBranch = parentBranch;
            this.goToBranch = goToBranch;
        }
        
        public Predicate<A> getTester() {
            return tester;
        }
        
        public DialogueBranch<A> getParentBranch() {
            return parentBranch;
        }
        
        public DialogueBranch<B> getGoToBranch() {
            return goToBranch;
        }
    }
}
