package org.comroid.crystalshard.util.commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a Command or a Class as part of a CommandGroup.
 * <p>
 * CommandGroups with duplicate names are merged with the latest-registered parameters.
 * Names for merging have to be equal after {@link String#equals(Object)}.
 * <p>
 * If a {@link #ordinal()} value of groups with different names are equal,
 * the registration method will throw an {@link IllegalStateException}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface CommandGroup {
    /**
     * Defines the name of the command group.
     *
     * @return The name of the command group.
     */
    String name() default CommandHandler.NO_GROUP;

    /**
     * Defines the description of the command group.
     *
     * @return The description  of the command group.
     */
    String description() default CommandHandler.NO_GROUP;

    /**
     * Defines the group's ordinal index for being listed in a help command.
     *
     * @return The group's ordinal index.
     */
    int ordinal() default Integer.MAX_VALUE;
}
