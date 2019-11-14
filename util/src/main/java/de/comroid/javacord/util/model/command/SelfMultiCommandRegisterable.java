package de.comroid.javacord.util.model.command;

import java.util.Collection;

import de.comroid.javacord.util.commands.CommandRepresentation;

public interface SelfMultiCommandRegisterable<Self extends SelfMultiCommandRegisterable> {
    Self registerCommandTarget(Object target);

    Self unregisterCommandTarget(Object target);

    Collection<CommandRepresentation> getCommands();

    // Extensions
    @SuppressWarnings("unchecked")
    default Self registerCommands(Object... targets) {
        for (Object target : targets)
            registerCommandTarget(target);
        return (Self) this;
    }

    @SuppressWarnings("unchecked")
    default Self unregisterCommands(Object... targets) {
        for (Object target : targets)
            unregisterCommandTarget(target);
        return (Self) this;
    }
}
