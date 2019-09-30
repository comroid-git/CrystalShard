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
    public <T> T getValue(JsonTrait<?, T> trait) {
        //noinspection unchecked
        return (T) values.getOrDefault(trait, null);
    }

    protected void updateFromJson(final JsonNode data) {
        for (JsonTrait<?, ?> jsonTrait : possibleTraits()) {
            String fieldName = jsonTrait.fieldName();
            
            if (fieldName.equals("#root"))
                values.put(jsonTrait, )
        }
    }

    private @Nullable Object chooseValue(Class<?> fieldType, String value) {
        try {
            if (String.class.isAssignableFrom(fieldType)) return value;
            if (Integer.TYPE.isAssignableFrom(fieldType)) return Integer.parseInt(value);
            if (Long.TYPE.isAssignableFrom(fieldType)) return Long.parseLong(value);
            if (URL.class.isAssignableFrom(fieldType)) return new URL(value);
            if (URI.class.isAssignableFrom(fieldType)) return new URI(value);
            if (Color.class.isAssignableFrom(fieldType)) return Color.getColor(value);

            throw new ClassNotFoundException("Unknown deserialization type: " + fieldType);
        } catch (Exception e) {
            throw new RuntimeException("Type Selection Exception", e);
        }
    }

    private @Nullable Object chooseValue(Class<?> fieldType, final JsonNode fromNode, Object defaultValue) {
        String str;

        try {
            if (String.class.isAssignableFrom(fieldType)) return fromNode.asText(defaultValue.toString());
            if (Integer.TYPE.isAssignableFrom(fieldType)) return fromNode.asInt((int) defaultValue);
            if (Long.TYPE.isAssignableFrom(fieldType)) return fromNode.asLong((long) defaultValue);
            if (URL.class.isAssignableFrom(fieldType))
                return (defaultValue instanceof URL)
                        ? defaultValue.toString().equals(str = fromNode.asText(defaultValue.toString()))
                        ? defaultValue
                        : new URL(str)
                        : new URL(fromNode.asText(defaultValue.toString()));
            if (URI.class.isAssignableFrom(fieldType))
                return (defaultValue instanceof URI)
                        ? defaultValue.toString().equals(str = fromNode.asText(defaultValue.toString()))
                        ? defaultValue
                        : new URI(str)
                        : new URI(fromNode.asText(defaultValue.toString()));
            if (Color.class.isAssignableFrom(fieldType)) return new Color(fromNode.asInt((int) defaultValue));

            /*
             todo Implement entity generation

             help annotation: SerializedCollection.class

             include:
             - int[]
            */
            if (AbstractJsonDeserializable.class.isAssignableFrom(fieldType)) {

            }

            log.at(Level.FINEST).log("Unknown deserialization type: %s", fieldType);
            return defaultValue;
        } catch (Exception e) {
            throw new RuntimeException("Type Selection Exception", e);
        }
    }
}
