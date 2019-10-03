package de.kaleidox.crystalshard.abstraction.serialization;

import java.awt.Color;
import java.net.URI;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import de.kaleidox.crystalshard.abstraction.AbstractApiBound;
import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.util.model.serialization.JsonDeserializable;
import de.kaleidox.crystalshard.util.model.serialization.JsonTrait;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.flogger.FluentLogger;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractJsonDeserializable extends AbstractApiBound implements JsonDeserializable {
    private static final FluentLogger log = FluentLogger.forEnclosingClass();
    private Map<JsonTrait, Object> values;

    protected AbstractJsonDeserializable(Discord api, JsonNode data) {
        super(api);

        values = new ConcurrentHashMap<>();

        updateFromJson(data);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <S, T> T getTraitValue(JsonTrait<S, T> trait) {
        S val;

        return (val = (S) values.getOrDefault(trait, null)) == null ? null : trait.map(val);
    }

    protected void updateFromJson(final JsonNode data) {
        for (JsonTrait<?, ?> jsonTrait : possibleTraits()) {
            String fieldName = jsonTrait.fieldName();
            JsonNode field = data.path(fieldName);

            if (field.isMissingNode()) {
                log.at(Level.FINER).log("[%s] Field %s is missing; skipping!", toString(), fieldName);
                continue;
            }

            if (fieldName.equals("#root"))
                values.put(jsonTrait, jsonTrait.extract(field));
        }
    }
}
