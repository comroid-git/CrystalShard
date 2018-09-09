package de.kaleidox.util.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * This annotation marks a certain parameter to never be {@code null}. The method may throw a
 * {@link NullPointerException} if this parameter is {@code null}.
 */
@Target(ElementType.PARAMETER)
public @interface NotNull {
}
