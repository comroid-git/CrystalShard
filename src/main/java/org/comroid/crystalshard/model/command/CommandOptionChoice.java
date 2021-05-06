package org.comroid.crystalshard.model.command;

import org.comroid.api.ContextualProvider;
import org.comroid.api.Named;
import org.comroid.api.ValueType;
import org.comroid.crystalshard.model.AbstractDataContainer;
import org.comroid.mutatio.ref.Reference;
import org.comroid.uniform.node.UniNode;
import org.comroid.uniform.node.UniValueNode;
import org.comroid.util.StandardValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.Nullable;

public abstract class CommandOptionChoice<T> extends AbstractDataContainer implements Named {
    public static final GroupBind<CommandOptionChoice<?>> TYPE
            = BASETYPE.subGroup("application-command-option-choice", CommandOptionChoice::resolve);
    public static final VarBind<CommandOptionChoice<?>, String, String, String> NAME
            = TYPE.createBind("name")
            .extractAs(StandardValueType.STRING)
            .build();
    public final Reference<String> name = getComputedReference(NAME);
    public final Reference<T> value = getValueReference();

    @Override
    public String getName() {
        return name.assertion();
    }

    public T getValue() {
        return getValueReference().assertion();
    }

    protected abstract Reference<T> getValueReference();

    private CommandOptionChoice(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);
    }

    private static CommandOptionChoice<?> resolve(ContextualProvider context, UniNode data) {
        UniValueNode target = data.get("value").asValueNode();
        ValueType<?> heldType = target.getHeldType();
        if (heldType.equals(StandardValueType.STRING))
            return new OfString(context, data);
        if (heldType.isNumeric())
            return new OfInteger(context, data);
        throw new IllegalArgumentException("Invalid value type: " + heldType.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CommandOptionChoice))
            return false;
        final CommandOptionChoice<?> other = (CommandOptionChoice<?>) o;
        return name.equals(other.name)
                && value.equals(other.value);
    }

    public enum Type {
        STRING, INTEGER
    }

    public static final class OfString extends CommandOptionChoice<String> {
        @RootBind
        public static final GroupBind<OfString> TYPE
                = BASETYPE.subGroup("application-command-option-choice:string");
        public static final VarBind<OfString, String, String, String> VALUE
                = TYPE.createBind("value")
                .extractAs(StandardValueType.STRING)
                .build();

        @Override
        protected Reference<String> getValueReference() {
            return getComputedReference(VALUE);
        }

        private OfString(ContextualProvider context, @Nullable UniNode initialData) {
            super(context, initialData);
        }
    }

    public static final class OfInteger extends CommandOptionChoice<Integer> {
        @RootBind
        public static final GroupBind<OfInteger> TYPE
                = BASETYPE.subGroup("application-command-option-choice:integer");
        public static final VarBind<OfInteger, Integer, Integer, Integer> VALUE
                = TYPE.createBind("value")
                .extractAs(StandardValueType.INTEGER)
                .build();

        @Override
        protected Reference<Integer> getValueReference() {
            return getComputedReference(VALUE);
        }

        private OfInteger(ContextualProvider context, @Nullable UniNode initialData) {
            super(context, initialData);
        }
    }
}
