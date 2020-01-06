package org.comroid.crystalshard.adapter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.comroid.crystalshard.api.Discord;
import org.comroid.crystalshard.util.model.serialization.JsonDeserializable;

import com.alibaba.fastjson.JSONObject;

/**
 * Determines the requirement for a Constructor with available parameters.
 */
@Repeatable(Constructors.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Constructor {
    /**
     * The parameters required for the constructor.
     * By default, this definition is the equivalent of the required constructor for any {@link JsonDeserializable}.
     *
     * @return The required parameters.
     */
    Class<?>[] value() default {Discord.class, JSONObject.class};
}
