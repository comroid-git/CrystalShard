package org.comroid.crystalshard.util.model.serialization;

import java.util.Optional;
import java.util.Set;

import org.comroid.crystalshard.adapter.Constructor;
import org.comroid.crystalshard.api.model.ApiBound;

import com.alibaba.fastjson.JSONObject;

@Constructor
@SuppressWarnings("rawtypes")
public interface JsonDeserializable extends ApiBound, Cloneable {
    Set<JSONBinding> bindings();

    <S, T> T getBindingValue(JSONBinding<?, S, ?, T> trait);

    default <T> Optional<T> wrapBindingValue(JSONBinding<?, ?, ?, T> trait) {
        return Optional.ofNullable(getBindingValue(trait));
    }

    Set<JSONBinding> updateFromJson(final JSONObject data);

    Set<JSONBinding> initialBindings();

    default String jsonString() {
        return jsonString(true);
    }

    default String jsonString(boolean includeNulls) {
        return jsonString(bindings(), includeNulls);
    }

    @SuppressWarnings("unchecked")
    default String jsonString(Set<JSONBinding> with, boolean includeNulls) {
        JSONObject json = new JSONObject();

        if (includeNulls) with.forEach(binding -> json.put(binding.fieldName(), getBindingValue(binding)));
        else with.forEach(binding -> wrapBindingValue(binding).ifPresent(value -> json.put(binding.fieldName(), value)));

        return json.toJSONString();
    }
}
