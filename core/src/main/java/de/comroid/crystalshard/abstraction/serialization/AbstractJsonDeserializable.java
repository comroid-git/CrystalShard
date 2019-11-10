package de.comroid.crystalshard.abstraction.serialization;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import de.comroid.crystalshard.CrystalShard;
import de.comroid.crystalshard.abstraction.AbstractApiBound;
import de.comroid.crystalshard.adapter.Adapter;
import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.api.entity.Snowflake;
import de.comroid.crystalshard.util.model.serialization.JSONBinding;
import de.comroid.crystalshard.util.model.serialization.JsonDeserializable;
import de.comroid.crystalshard.util.model.serialization.JSONBindingLocation;

import com.alibaba.fastjson.JSONObject;
import com.google.common.flogger.FluentLogger;

import static de.comroid.crystalshard.CrystalShard.PLEASE_REPORT;

public abstract class AbstractJsonDeserializable extends AbstractApiBound implements JsonDeserializable {
    private static final FluentLogger log = FluentLogger.forEnclosingClass();

    protected final Set<JSONBinding> possibleTraits;
    
    private Map<JSONBinding, Object> values;

    protected AbstractJsonDeserializable(Discord api, JSONObject data) {
        super(api);

        JSONBindingLocation traitsClass = Adapter.getApiClass(getClass())
                .orElseThrow(() -> new NoSuchElementException("Could not find API class of " + this + "." + PLEASE_REPORT))
                .getAnnotation(JSONBindingLocation.class);

        if (traitsClass == null)
            throw new AssertionError("Could not determine @JsonTraits annotation for "
                    + getClass().getSimpleName() + "! Please open an issue at " + CrystalShard.ISSUES_URL);

        possibleTraits = Arrays.stream(traitsClass.value()
                .getFields())
                .filter(field -> Snowflake.JSON.class.isAssignableFrom(field.getType()))
                .filter(field -> Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers()))
                .map(field -> {
                    try {
                        return field.get(null);
                    } catch (IllegalAccessException e) {
                        throw new AssertionError("Could not access Traits", e);
                    }
                })
                .map(JSONBinding.class::cast)
                .collect(Collectors.toSet());

        values = new ConcurrentHashMap<>();

        updateFromJson(data);
    }

    @Override
    public Set<JSONBinding> bindings() {
        return possibleTraits;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <S, T> T getBindingValue(JSONBinding<?, S, ?, T> binding) {
        S val;
        return (val = (S) values.getOrDefault(binding, null)) == null ? null : binding.apply(api, val);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<JSONBinding> updateFromJson(final JSONObject json) {
        Set<JSONBinding> changed = new HashSet<>();

        for (JSONBinding binding : bindings()) {
            final String key = binding.fieldName();

            if (json.containsKey(key))
                values.put(binding, binding.extractValue(json));
        }
        
        return changed;
    }
}
