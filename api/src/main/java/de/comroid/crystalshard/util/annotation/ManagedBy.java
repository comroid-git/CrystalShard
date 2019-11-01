package de.comroid.crystalshard.util.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.comroid.crystalshard.api.listener.model.ListenerManager;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ManagedBy {
    Class<? extends ListenerManager> value();
}
