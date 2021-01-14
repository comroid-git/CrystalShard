package org.comroid.crystalshard.entity.channel;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.entity.EntityType;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.entity.channel.impl.AbstractTextChannel;
import org.comroid.mutatio.ref.Reference;
import org.comroid.mutatio.span.Span;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public final class PrivateChannel extends AbstractTextChannel implements TextChannel {
    @Override
    public long getID() {
        return 0;
    }

    @Override
    public EntityType getEntityType() {
        return null;
    }

    @Override
    public ContextualProvider getUnderlyingContextualProvider() {
        return null;
    }

    @Override
    public GroupBind<Snowflake> getRootBind() {
        return null;
    }

    @Override
    public Class<? extends Snowflake> getRepresentedType() {
        return null;
    }

    @Override
    public Set<VarBind<? extends Snowflake, Object, ?, Object>> updateFrom(UniObjectNode node) {
        return null;
    }

    @Override
    public Set<VarBind<? extends Snowflake, Object, ?, Object>> initiallySet() {
        return null;
    }

    @Override
    public <T> Optional<Reference<T>> getByName(String name) {
        return Optional.empty();
    }

    @Override
    public UniObjectNode toObjectNode(UniObjectNode node) {
        return null;
    }

    @Override
    public <T> @Nullable T put(VarBind<? extends Snowflake, T, ?, ?> bind, T value) {
        return null;
    }

    @Override
    public <T, X> @Nullable T put(VarBind<? extends Snowflake, X, ?, T> bind, Function<T, X> parser, T value) {
        return null;
    }

    @Override
    public <E> Reference<Span<E>> getExtractionReference(String name) {
        return null;
    }

    @Override
    public <T> Reference<T> getComputedReference(String name) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public Object get(Object key) {
        return null;
    }

    @Nullable
    @Override
    public Object put(String key, Object value) {
        return null;
    }

    @Override
    public Object remove(Object key) {
        return null;
    }

    @Override
    public void putAll(@NotNull Map<? extends String, ?> m) {

    }

    @Override
    public void clear() {

    }

    @NotNull
    @Override
    public Set<String> keySet() {
        return null;
    }

    @NotNull
    @Override
    public Collection<Object> values() {
        return null;
    }

    @NotNull
    @Override
    public Set<Entry<String, Object>> entrySet() {
        return null;
    }
}
