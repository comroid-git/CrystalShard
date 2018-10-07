package de.kaleidox.crystalshard.util.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * This annotation marks a certain parameter of a method as {@code NULLABLE}, which means the parameter may be {@code
 * null}. In this case, the method must handle all {@link NullPointerException} that come from this parameter.
 * When annotating a Method, this annotation guarantees for the method not to return null.
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
public @interface Nullable { }
