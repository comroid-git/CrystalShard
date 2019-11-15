package de.comroid.crystalshard.api.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.jetbrains.annotations.ApiStatus.Internal;

@Internal
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface InitializedBy {
    Class<? extends Enum<? extends EventHandler.Initializer>> value();

    String name() default "INSTANCE";
}
