package de.kaleidox.crystalshard.util.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.Collection;

/**
 * This annotation marks a {@link Collection} or {@code Array} parameter to enable it to contain {@code null} items.
 *
 * @see Nullable
 */
@Target(ElementType.PARAMETER)
public @interface MayContainNull { }
