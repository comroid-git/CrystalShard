package de.kaleidox.util.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.Collection;

/**
 * This annotation marks a {@link Collection} or {@code Array} parameter to disable it to contain {@code null} items.
 *
 * @see NotNull
 */
@Target(ElementType.PARAMETER)
public @interface DontContainNull {
}
