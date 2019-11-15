package de.comroid.crystalshard.adapter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Determines more than one required constructor.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Constructors {
    /**
     * An array of constructors.
     * By default, this contains only the default definition of {@link Constructor}.
     * 
     * @return All required constructors.
     */
    Constructor[] value() default @Constructor;
}
