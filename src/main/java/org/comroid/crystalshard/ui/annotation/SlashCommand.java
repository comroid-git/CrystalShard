package org.comroid.crystalshard.ui.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SlashCommand {
    String name() default "";

    String description() default "No Description'";

    boolean useGlobally() default true;
}
