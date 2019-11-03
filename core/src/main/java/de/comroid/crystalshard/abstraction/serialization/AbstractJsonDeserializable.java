package de.comroid.crystalshard.abstraction.serialization;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.stream.Collectors;

import de.comroid.crystalshard.CrystalShard;
import de.comroid.crystalshard.abstraction.AbstractApiBound;
import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.api.entity.Snowflake;
import de.comroid.crystalshard.util.model.serialization.JsonBinding;
import de.comroid.crystalshard.util.model.serialization.JsonDeserializable;
import de.comroid.crystalshard.util.model.serialization.JsonTraits;

import com.alibaba.fastjson.JSONObject;
import com.google.common.flogger.FluentLogger;

public abstract class AbstractJsonDeserializable extends AbstractApiBound implements JsonDeserializable {
    private static final FluentLogger log = FluentLogger.forEnclosingClass();

    protected final Set<JsonBinding> possibleTraits;
    
    private Map<JsonBinding, Object> values;

    protected AbstractJsonDeserializable(Discord api, JSONObject data) {
        super(api);
        
        JsonTraits traitsClass = getClass().getAnnotation(JsonTraits.class);

        if (traitsClass == null)
            throw new AssertionError("Could not determine @JsonTraits annotation for "
                    + getClass().getSimpleName() + "! Please open an issue at " + CrystalShard.ISSUES_URL);

        possibleTraits = Arrays.stream(traitsClass.value()
                .getFields())
                .filter(field -> Snowflake.Trait.class.isAssignableFrom(field.getType()))
                .filter(field -> Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers()))
                .map(field -> {
                    try {
                        return field.get(null);
                    } catch (IllegalAccessException e) {
                        throw new AssertionError("Could not access Traits", e);
                    }
                })
                .map(JsonBinding.class::cast)
                .map(trait -> trait.cloneWithApi(api))
                .collect(Collectors.toSet());
        

        values = new ConcurrentHashMap<>();

        updateFromJson(data);
    }

    @Override
    public Set<JsonBinding> possibleTraits() {
        return possibleTraits;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <S, T> T getTraitValue(JsonBinding<?, S, ?, T> trait) {
        S val;

        return (val = (S) values.getOrDefault(trait, null)) == null ? null : trait.apply(val);
    }

    @Override
    public Set<JsonBinding> updateFromJson(final JSONObject data) {
        Set<JsonBinding> changed = new HashSet<>();
        
        for (JsonBinding binding : possibleTraits()) {
            final String fieldName = binding.fieldName();
            final JsonNode field = data.path(fieldName);

            if (field.isMissingNode()) {
                log.at(Level.FINER).log("[%s] Field %s is missing; skipping!", toString(), fieldName);
                continue;
            }

            final Object after = binding.extract(field);
            final Object before = values.put(binding, after);
            
            if (!before.equals(after))
                changed.add(binding);
        }
        
        return changed;
    }
}
