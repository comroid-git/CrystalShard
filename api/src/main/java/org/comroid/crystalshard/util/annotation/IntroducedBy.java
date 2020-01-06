package org.comroid.crystalshard.util.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface IntroducedBy {
    ImplementationSource value();

    String docs() default "";

    enum ImplementationSource {
        /**
         * Is implemented by CrystalShard API
         */
        PRODUCTION,

        /**
         * Is implemented by Discord API
         */
        API,

        /**
         * Is a standard getter
         */
        GETTER,

        /**
         * Is a standard setter
         */
        SETTER
    }
}
