package de.kaleidox.crystalshard.util.discord.ui;

import de.kaleidox.crystalshard.util.discord.ui.response.ResponseElement;
import de.kaleidox.util.objects.NamedItem;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class DialoguePassthrough<A> extends DialogueBranch<A> {
    private final Consumer<List<NamedItem>> responsesConsumer;

    /**
     * Creates a new DialogueEndpoint object.
     *
     * @param questionElement   The the question element to execute here.
     * @param responsesConsumer A Consumer to handle the list of responses at the end.
     */
    public DialoguePassthrough(
            ResponseElement<A> questionElement,
            Consumer<List<NamedItem>> responsesConsumer) {
        super(questionElement);
        this.responsesConsumer = responsesConsumer;
    }

    /**
     * Adds a new handling possibility to the current branch.
     *
     * @param tester          A Predicate to test the result of the question element.
     * @param followingBranch The branch that follows if the question element is tested true.
     * @param <B>             The type of the next branch.
     * @return The instance for chaining methods.
     */
    @Override
    public <B> DialoguePassthrough<A> addOption(
            Predicate<A> tester,
            DialogueBranch<B> followingBranch) {
        return addOption(new Option<B>(tester, this, followingBranch));
    }

    /**
     * Adds a new handling possibility to the current branch.
     *
     * @param option The pre-built {@link Option} to add.
     * @param <B>    The type of the next branch.
     * @return The instance for chaining methods.
     */
    @Override
    public <B> DialoguePassthrough<A> addOption(Option<B> option) {
        options.add(option);

        return this;
    }

    @Override
    protected CompletableFuture<Void> runPassthrough(List<NamedItem> collectedItems) {
        return CompletableFuture.supplyAsync(() -> {
            responsesConsumer.accept(collectedItems);
            return null;
        });
    }
}
