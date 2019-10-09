package de.kaleidox.crystalshard.core.api.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.Optional;
import java.util.function.Function;

import de.kaleidox.crystalshard.adapter.Adapter;
import de.kaleidox.crystalshard.api.entity.Snowflake;
import de.kaleidox.crystalshard.util.model.serialization.JsonDeserializable;

import com.fasterxml.jackson.databind.JsonNode;

public interface Cacheable extends JsonDeserializable {
    default void update(JsonNode data) {
        updateFromJson(data);
    }

    static <S extends Cacheable, P extends Cacheable & Snowflake> Optional<CacheInformation<P>> getCacheInfo(S of) {
        final Class<? extends Cacheable> klass = of.getClass();

        try {
            for (Field field : klass.getFields()) {
                final CacheInformation.Marker annotation = field.getAnnotation(CacheInformation.Marker.class);

                if (annotation != null)
                    return field.get(null);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    } 

    static <S extends Cacheable, P extends Cacheable & Snowflake> CacheInformation<P> makeSubcacheableInfo(
            Class<P> parentClass,
            Function<S, P> parentExtractor
    ) {
        return Adapter.create(CacheInformation.class, 1, parentClass, parentExtractor);
    }

    static <S extends Cacheable, P extends Cacheable & Snowflake> CacheInformation<P> makeSingletonCacheableInfo(
            Class<P> parentClass,
            Function<S, P> parentExtractor
    ) {
        return Adapter.create(CacheInformation.class, 2, parentClass, parentExtractor);
    }

    interface CacheInformation<P extends Cacheable & Snowflake> {
        Class<P> getParentClass();

        P getParent();

        default long getIDfromParent() {
            return getParent().getID();
        }
        
        @Target(ElementType.FIELD)
        @Retention(RetentionPolicy.RUNTIME)
        @interface Marker {
        }
    }
}
