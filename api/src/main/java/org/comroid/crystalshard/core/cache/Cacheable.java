package org.comroid.crystalshard.core.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.Optional;
import java.util.function.Function;

import org.comroid.crystalshard.adapter.Adapter;
import org.comroid.crystalshard.api.entity.Snowflake;
import org.comroid.crystalshard.util.model.serialization.JsonDeserializable;

import com.alibaba.fastjson.JSONObject;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.ApiStatus.Internal;

public interface Cacheable extends JsonDeserializable {
    default void update(JSONObject data) {
        updateFromJson(data);
    }

    static <S extends Cacheable, P extends Cacheable & Snowflake> Optional<CacheInformation<P>> getCacheInfo(S of) {
        final Class<? extends Cacheable> klass = of.getClass();

        try {
            for (Field field : klass.getFields()) {
                final CacheInformation.Marker annotation = field.getAnnotation(CacheInformation.Marker.class);

                if (annotation != null)
                    //noinspection unchecked
                    return Optional.ofNullable((CacheInformation<P>) field.get(null));
            }
        } catch (IllegalAccessException ignored) {
        }
        
        return Optional.empty();
    } 

    static <S extends Cacheable, P extends Cacheable & Snowflake> CacheInformation<P> makeSubcacheableInfo(
            Class<P> parentClass,
            Function<S, P> parentExtractor
    ) {
        return Adapter.require(CacheInformation.class, 1, parentClass, parentExtractor);
    }

    static <S extends Cacheable, P extends Cacheable & Snowflake> CacheInformation<P> makeSingletonCacheableInfo(
            Class<P> parentClass,
            Function<S, P> parentExtractor
    ) {
        return Adapter.require(CacheInformation.class, 2, parentClass, parentExtractor);
    }

    @Internal
    interface CacheInformation<P extends Cacheable & Snowflake> {
        Class<P> getParentClass();

        P getParent();

        default long getIDfromParent() {
            return getParent().getID();
        }
        
        @MagicConstant(intValues = {
                0, // subcache
                1  // singleton
        })
        int type();
        
        @Target(ElementType.FIELD)
        @Retention(RetentionPolicy.RUNTIME)
        @interface Marker {
        }
    }
}
