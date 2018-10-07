package de.kaleidox.crystalshard.util.discord.ui;

import de.kaleidox.crystalshard.util.objects.markers.NamedItem;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * This class represents an Endpoint in a dialogue. When an instance of this class is used as a new branch withing an {@link
 * de.kaleidox.crystalshard.util.discord.ui.DialogueBranch.Option}, that option will lead to the end of the branch. The instance requires a Consumer that will
 * be able to consume an ordered list of the responses within the Dialogue.
 * <p>
 * The endpoint will be automatically run when an endpoint is hit, feeding the corresponding List of items.
 */
@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class DialogueEndpoint extends DialogueBranch<Void> {
    private final Consumer<List<NamedItem>> responsesConsumer;
    
    /**
     * Creates a new DialogueEndpoint object.
     *
     * @param responsesConsumer A Consumer to handle the list of responses at the end.
     */
    public DialogueEndpoint(Consumer<List<NamedItem>> responsesConsumer) {
        super(null);
        this.responsesConsumer = responsesConsumer;
    }
    
    // Override Methods
    @Override
    protected CompletableFuture<Void> runEndpoint(List<NamedItem> collectedItems) {
        return CompletableFuture.supplyAsync(() -> {
            responsesConsumer.accept(collectedItems);
            return null;
        });
    }
}
