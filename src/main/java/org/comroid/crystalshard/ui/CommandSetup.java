package org.comroid.crystalshard.ui;

import org.comroid.api.IntegerAttribute;
import org.comroid.crystalshard.entity.command.Command;
import org.comroid.crystalshard.model.command.CommandOption;
import org.comroid.crystalshard.model.command.CommandOptionChoice;
import org.comroid.crystalshard.ui.annotation.Choice;
import org.comroid.crystalshard.ui.annotation.Option;
import org.comroid.crystalshard.ui.annotation.SlashCommand;
import org.comroid.uniform.node.UniArrayNode;
import org.comroid.uniform.node.UniObjectNode;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.unmodifiableSet;

public class CommandSetup {
    private final InteractionCore core;
    final Map<String, CommandDefinition> definedCommands = new ConcurrentHashMap<>();
    final Map<Long, Set<String>> guildBindings = new ConcurrentHashMap<>();

    public boolean hasCommand(String name) {
        return definedCommands.containsKey(name);
    }

    public boolean hasCommand(long guildId, String name) {
        return hasCommand(name) && getBindings(guildId).contains(name);
    }

    public @Nullable CommandDefinition getCommand(String name) {
        return definedCommands.get(name);
    }

    public Set<CommandDefinition> getAllDefinitions() {
        return unmodifiableSet(new HashSet<>(definedCommands.values()));
    }

    public Set<CommandDefinition> getGlobalDefinitions() {
        return unmodifiableSet(definedCommands.values()
                .stream()
                .filter(CommandDefinition::useGlobally)
                .collect(Collectors.toSet()));
    }

    public Set<CommandDefinition> getGuildDefinitions(long guildId) {
        return unmodifiableSet(getBindings(guildId)
                .stream()
                .map(this::getCommand)
                .collect(Collectors.toSet()));
    }

    public boolean addGuildDefinition(long guildId, CommandDefinition command) {
        return getBindings(guildId).add(command.getName());
    }

    private Set<String> getBindings(long guildId) {
        return guildBindings.computeIfAbsent(guildId, k -> new HashSet<>());
    }

    CommandSetup(InteractionCore core) {
        this.core = core;
    }

    public CommandSetup readClass(Class<?> type) {
        return readClass(null, type);
    }

    public <T> CommandSetup readClass(T target) {
        return readClass(target, target.getClass());
    }

    public <T> CommandSetup readClass(T target, Class<? extends T> type) {
        for (Method method : type.getDeclaredMethods())
            if (target != null || Modifier.isStatic(method.getModifiers()))
                readMethod(target, method);
        for (Class<?> root : type.getDeclaredClasses())
            if (root.isAnnotationPresent(SlashCommand.class))
                readTree(target, root);
        return this;
    }

    private void readTree(Object target, Class<?> root) {
        if (!root.isAnnotationPresent(SlashCommand.class))
            return;
        final SlashCommand cmdDef = root.getAnnotation(SlashCommand.class);
        final UniObjectNode cmd = core.getSerializer().createObjectNode();

        cmd.put(Command.NAME, (cmdDef.name().isEmpty() ? prefabName(root.getName()) : cmdDef.name()).toLowerCase());
        cmd.put(Command.DESCRIPTION, cmdDef.description());

        readSubOptions(cmd, root);

        final CommandDefinition definition = new CommandDefinition(core, cmd, target, root);
        definedCommands.put(definition.getName(), definition);
    }

    private void readSubOptions(UniObjectNode me, Class<?> root) {
        if (!root.isAnnotationPresent(SlashCommand.class))
            return;
        final SlashCommand def = root.getAnnotation(SlashCommand.class);

        me.put(CommandOption.NAME, (def.name().isEmpty() ? prefabName(root.getSimpleName()) : def.name()).toLowerCase());
        me.put(CommandOption.DESCRIPTION, def.description());
        me.put(CommandOption.OPTION_TYPE, CommandOption.Type.SUB_COMMAND_GROUP);

        final UniArrayNode options = me.putArray(CommandOption.OPTIONS);
        Stream.concat(Arrays.stream(root.getDeclaredClasses()), Arrays.stream(root.getDeclaredMethods()))
                .filter(it -> it.isAnnotationPresent(SlashCommand.class))
                .forEach(it -> {
                    final UniObjectNode option = options.addObject();
                    if (it instanceof Class)
                        readSubOptions(option, (Class<?>) it);
                    else if (it instanceof Method)
                        readSubOptions(option, (Method) it);
                    else throw new AssertionError();
                });
    }

    private void readSubOptions(UniObjectNode me, Method method) {
        if (!method.isAnnotationPresent(SlashCommand.class))
            return;
        final SlashCommand def = method.getAnnotation(SlashCommand.class);

        me.put(CommandOption.NAME, (def.name().isEmpty() ? prefabName(method.getName()) : def.name()).toLowerCase());
        me.put(CommandOption.DESCRIPTION, def.description());
        me.put(CommandOption.OPTION_TYPE, CommandOption.Type.SUB_COMMAND);

        readMethodOptions(me, method);
    }

    private void readMethod(Object target, Method method) {
        if (!method.isAnnotationPresent(SlashCommand.class))
            return;
        final SlashCommand cmdDef = method.getAnnotation(SlashCommand.class);
        final UniObjectNode cmd = core.getSerializer().createObjectNode();

        cmd.put(Command.NAME, (cmdDef.name().isEmpty() ? prefabName(method.getName()) : cmdDef.name()).toLowerCase());
        cmd.put(Command.DESCRIPTION, cmdDef.description());

        readMethodOptions(cmd, method);

        final CommandDefinition definition = new CommandDefinition(core, cmd, target, method);
        definedCommands.put(definition.getName(), definition);
    }

    private void readMethodOptions(UniObjectNode me, Method method) {
        final UniArrayNode options = me.putArray(Command.OPTIONS);
        for (Parameter param : method.getParameters()) {
            if (!param.isAnnotationPresent(Option.class))
                continue; // handle non-present annotation => does it need handling though?

            final UniObjectNode option = options.addObject();
            final Option optDef = param.getAnnotation(Option.class);

            option.put(CommandOption.OPTION_TYPE, CommandOption.Type.typeOf(param.getType()));
            option.put(CommandOption.NAME, (optDef.name().isEmpty() ? prefabName(param.getName()) : optDef.name()).toLowerCase());
            option.put(CommandOption.DESCRIPTION, optDef.description());
            option.put(CommandOption.IS_DEFAULT, optDef.first());
            option.put(CommandOption.IS_REQUIRED, optDef.required());

            if (IntegerAttribute.class.isAssignableFrom(param.getType())) {
                final UniArrayNode choices = option.putArray(CommandOption.CHOICES);
                for (Object it : param.getType().getEnumConstants()) {
                    final IntegerAttribute each = (IntegerAttribute) it;
                    final UniObjectNode choice = choices.addObject();

                    choice.put(CommandOptionChoice.NAME, each.getName());
                    choice.put(CommandOptionChoice.OfInteger.VALUE, each.getValue());
                }
            } else {
                final Choice[] choicesDef = optDef.choices();
                if (choicesDef.length == 0)
                    continue;
                final UniArrayNode choices = option.putArray(CommandOption.CHOICES);
                for (Choice choiceDef : choicesDef) {
                    final UniObjectNode choice = choices.addObject();

                    choice.put(CommandOptionChoice.NAME, choiceDef.name());
                    final String cv = choiceDef.value();
                    if (cv.matches("\\d+"))
                        choice.put(CommandOptionChoice.OfInteger.VALUE, Integer.parseInt(cv));
                    else choice.put(CommandOptionChoice.OfString.VALUE, cv);
                }
            }
        }
    }

    private static String prefabName(String name) {
        if (name.matches("^[\\w-]{3,32}$"))
            return name;
        StringBuilder str = new StringBuilder();
        for (char c : name.toCharArray())
            if (Character.isLetter(c))
                str.append(c);
            else str.append('-');
        return str.toString();
    }
}
