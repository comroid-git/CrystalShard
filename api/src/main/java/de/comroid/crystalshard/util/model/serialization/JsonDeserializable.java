package de.comroid.crystalshard.util.model.serialization;

import java.util.Optional;
import java.util.Set;

import de.comroid.crystalshard.adapter.Constructor;
import de.comroid.crystalshard.api.model.ApiBound;

import com.alibaba.fastjson.JSONObject;
import org.jetbrains.annotations.Nullable;

@Constructor
@SuppressWarnings("rawtypes")
public interface JsonDeserializable extends ApiBound, Cloneable {
    Set<JSONBinding> bindings();

    <S, T> @Nullable T getBindingValue(JSONBinding<?, S, ?, T> trait);

    default <T> Optional<T> wrapBindingValue(JSONBinding<?, ?, ?, T> trait) {
        return Optional.ofNullable(getBindingValue(trait));
    }
    
    Set<JSONBinding> updateFromJson(final JSONObject data);

    @SuppressWarnings("unchecked")
    default String jsonString() {
        JSONObject json = new JSONObject();

        bindings().forEach(binding -> json.put(binding.fieldName(), getBindingValue(binding)));

        return json.toJSONString();
    }
}
