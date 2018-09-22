package de.kaleidox.util.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * This annotation marks a certain parameter to never be {@code null}. The method may throw a {@link
 * NullPointerException} if this parameter is {@code null}.
 * When annotating a Method, this annotation guarantees for the method not to return null.
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
public @interface NotNull { }
