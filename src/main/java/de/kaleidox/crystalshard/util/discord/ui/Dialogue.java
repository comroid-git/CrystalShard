package de.kaleidox.crystalshard.util.discord.ui;

import de.kaleidox.crystalshard.util.discord.ui.response.ResponseElement;
import de.kaleidox.crystalshard.util.objects.markers.NamedItem;
import java.util.List;
import java.util.function.Supplier;

/**
 * This class is used to create a new Discord Dialogue
 *
 * @param <FirstBranchType> The type of the first {@link DialogueBranch}.
 */
@SuppressWarnings({"WeakerAccess", "FieldCanBeLocal", "unused"})
public class Dialogue<FirstBranchType> {
    private DialogueBranch<FirstBranchType> firstBranch;
    
    /**
     * Creates a new Discord Dialogue
     */
    public Dialogue() {
    }
    
    /**
     * Sets the first {@link DialogueBranch}.
     *
     * @param firstElement The first {@link ResponseElement} to be executed.
     * @return A {@link DialogueBranch} that is configured to be the root branch; therefore the first element.
     */
    public DialogueBranch<FirstBranchType> setFirstBranch(ResponseElement<FirstBranchType> firstElement) {
        DialogueBranch<FirstBranchType> firstBranch = new DialogueBranch<>(this, firstElement);
        this.firstBranch = firstBranch;
        
        return firstBranch;
    }
    
    /**
     * Starts the first branch. This is the last method to be called.
     *
     * @param listSupplier A Supplier to provide a new list where the collected items will get stored.
     * @see DialogueEndpoint
     */
    public void start(Supplier<List<NamedItem>> listSupplier) {
        firstBranch.start(listSupplier.get());
    }
}
