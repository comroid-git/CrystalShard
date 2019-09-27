package de.kaleidox.crystalshard.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Collection;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonData {
    String value() default "#root";

    Class<?> type() default Void.class;

    Class<? extends Collection> listType() default ArrayList.class;
}
