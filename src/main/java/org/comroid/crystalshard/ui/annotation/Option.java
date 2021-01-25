package org.comroid.crystalshard.ui.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Option {
    String name() default "";

    String description() default "No Description";

    boolean first() default false;

    boolean required() default false;

    Choice[] choices() default {};
}
