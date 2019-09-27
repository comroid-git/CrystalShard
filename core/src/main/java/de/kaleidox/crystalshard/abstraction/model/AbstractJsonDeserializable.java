package de.kaleidox.crystalshard.abstraction.model;

import java.awt.Color;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import de.kaleidox.crystalshard.abstraction.AbstractApiBound;
import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.util.model.serialization.JsonDeserializable;
import de.kaleidox.crystalshard.util.model.serialization.JsonTrait;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.flogger.FluentLogger;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractJsonDeserializable extends AbstractApiBound implements JsonDeserializable {
    private static final FluentLogger log = FluentLogger.forEnclosingClass();
    private Map<JsonTrait<?, ?>, ?> traits;

    protected AbstractJsonDeserializable(Discord api, JsonNode data) {
        super(api);

        traits = new ConcurrentHashMap<>();

        updateFromJson(data);
    }

    @Override
    public <T> T getTrait(JsonTrait<?, T> trait) {
        //noinspection unchecked
        return (T) traits.getOrDefault(trait, null);
    }

    protected void updateFromJson(final JsonNode data) {
        class FieldInformation {
            final JsonProperty json;
            final Field field;

            FieldInformation(JsonProperty json, Field field) {
                this.json = json;
                this.field = field;
            }
        }

        Class<? extends AbstractJsonDeserializable> myClass = this.getClass();

        Arrays.stream(myClass.getFields())
                // sort out irrelevant fields
                .filter(field -> !Modifier.isStatic(field.getModifiers()))
                .filter(field -> !Modifier.isFinal(field.getModifiers()))
                // sort out unannotated fields
                .filter(field -> field.isAnnotationPresent(JsonProperty.class))
                // add reflective access rights
                // commented out because json value fields should be PROTECTED and accessible from here
                //.peek(field -> field.setAccessible(true))
                // make fieldinformation object
                .map(field -> new FieldInformation(field.getAnnotation(JsonProperty.class), field))
                // sort out fields that are not in the json
                .filter(info -> data.has(info.json.value()))
                // fill fields
                .forEachOrdered(info -> fillField(info.field, data));
                // drop fieldinformation objects
                //.map(info -> info.field)
                // consume in order + remove access rights
                // commented out because json value fields should be PROTECTED and accessible from here
                //.forEachOrdered(field -> field.setAccessible(false));
    }

    protected void fillField(Field field, final JsonNode data) {
        Class<?> fieldType = field.getType();
        JsonProperty json = field.getAnnotation(JsonProperty.class);
        String jsonName = json.value().isEmpty() ? field.getName() : json.value();

        try {
            @Nullable Object defaultValue = null;
            if (!json.defaultValue().isEmpty())
                defaultValue = chooseValue(fieldType, json.defaultValue());

            field.set(this, chooseValue(fieldType, (jsonName.equals("#root") ? data : data.path(jsonName)), defaultValue));
        } catch (Exception e) {
            throw new RuntimeException("Reflective Operation Exception", e);
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
