package de.comroid.crystalshard.util.server.properties;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.util.model.SelfDefaultable;
import de.comroid.crystalshard.util.model.SelfDescribable;
import de.comroid.crystalshard.util.model.SelfDisplaynameable;
import de.comroid.crystalshard.util.markers.Value;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jetbrains.annotations.NotNull;

public final class PropertyGroup implements
        SelfDisplaynameable<PropertyGroup>,
        SelfDescribable<PropertyGroup>,
        SelfDefaultable<PropertyGroup, Object> {
    private final String name;
    private final ConcurrentHashMap<Long, Value> values;
    private Value defaultValue;
    private String displayName;
    private String description;

    public PropertyGroup(String name, Object defaultValue, String displayName, String description) {
        this.name = name;
        this.defaultValue = new Value(defaultValue);
        this.displayName = displayName;
        this.description = description;

        values = new ConcurrentHashMap<>();
    }

    public Value.Setter setValue(Guild server) {
        return setValue(server.getID());
    }

    public Value.Setter setValue(long serverId) {
        return getValue(serverId).setter();
    }

    public Value getValue(Guild server) {
        return getValue(server.getID());
    }

    public Value getValue(long serverId) {
        return values.compute(serverId, (k, v) -> {
            if (v == null) return defaultValue;
            return v;
        });
    }

    public <R> Function<Long, R> function(final Class<? extends R> targetType) {
        return serverId -> getValue(serverId).as(targetType);
    }

    public String getName() {
        return name;
    }

    @Override
    public PropertyGroup withDefaultValue(@NotNull Object value) {
        this.defaultValue = new Value(Objects.requireNonNull(value));
        return this;
    }

    @Override
    public Value getDefaultValue() {
        return defaultValue;
    }

    @Override
    public PropertyGroup withDescription(@NotNull String description) {
        this.description = description;
        return this;
    }

    @Override
    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

    @Override
    public PropertyGroup withDisplayName(@NotNull String displayName) {
        this.displayName = displayName;
        return this;
    }

    @Override
    public Optional<String> getDisplayName() {
        return Optional.ofNullable(displayName);
    }

    void serialize(JSONArray node) {
        values.forEach((id, value) -> {
            if (!value.asString().equals(defaultValue.asString())) {
                JSONObject json = new JSONObject();
                node.add(json);

                json.put("id", id);
                json.put("val", value.asString());
                json.put("type", (value.getValue() != null ? value.getValue() : "").getClass().getName());
            }
        });
    }
}
