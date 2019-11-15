package de.comroid.crystalshard.util.commands;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.api.entity.Snowflake;
import de.comroid.crystalshard.api.entity.channel.GuildChannel;
import de.comroid.crystalshard.api.entity.channel.GuildTextChannel;
import de.comroid.crystalshard.api.entity.channel.PrivateChannel;
import de.comroid.crystalshard.api.entity.channel.TextChannel;
import de.comroid.crystalshard.api.entity.emoji.Emoji;
import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.api.entity.message.Message;
import de.comroid.crystalshard.api.entity.user.User;
import de.comroid.crystalshard.api.event.guild.WrappedGuildEvent;
import de.comroid.crystalshard.api.event.multipart.message.MessageDeleteEvent;
import de.comroid.crystalshard.api.event.multipart.message.MessageEditEvent;
import de.comroid.crystalshard.api.event.multipart.message.MessageEvent;
import de.comroid.crystalshard.api.event.multipart.message.MessageSentEvent;
import de.comroid.crystalshard.api.listener.message.MessageDeleteListener;
import de.comroid.crystalshard.api.listener.message.MessageEditListener;
import de.comroid.crystalshard.api.listener.message.MessageSentListener;
import de.comroid.crystalshard.api.model.message.MessageAuthor;
import de.comroid.crystalshard.api.model.message.embed.Embed;
import de.comroid.crystalshard.api.model.permission.Permission;
import de.comroid.crystalshard.util.model.command.SelfBotOwnerIgnorable;
import de.comroid.crystalshard.util.model.command.SelfCommandChannelable;
import de.comroid.crystalshard.util.model.command.SelfCustomPrefixable;
import de.comroid.crystalshard.util.model.command.SelfMultiCommandRegisterable;
import de.comroid.crystalshard.util.model.command.SelfUnknownCommandRespondable;
import de.comroid.crystalshard.util.ui.embed.DefaultEmbedFactory;
import de.comroid.crystalshard.util.ui.messages.categorizing.CategorizedEmbed;
import de.comroid.crystalshard.util.ui.messages.paging.PagedEmbed;
import de.comroid.crystalshard.util.ui.messages.paging.PagedMessage;
import de.comroid.crystalshard.util.ui.reactions.InfoReaction;
import de.comroid.util.Util;

import com.google.common.flogger.FluentLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.lang.System.arraycopy;
import static java.lang.reflect.Modifier.isStatic;
import static de.comroid.crystalshard.adapter.Adapter.exceptionLogger;

public final class CommandHandler implements
        SelfMultiCommandRegisterable<CommandHandler>,
        SelfCommandChannelable<CommandHandler>,
        SelfCustomPrefixable<CommandHandler>,
        SelfBotOwnerIgnorable<CommandHandler>,
        SelfUnknownCommandRespondable<CommandHandler> {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();
    static final String NO_GROUP = "@NoGroup#";

    private final Discord api;
    private final Map<String, CommandRepresentation> commands = new ConcurrentHashMap<>();
    private final Map<Long, long[]> responseMap = new ConcurrentHashMap<>();

    public String[] prefixes;
    public boolean autoDeleteResponseOnCommandDeletion;
    public boolean useBotMentionAsPrefix;
    private Supplier<Embed> embedSupplier = null;
    private @Nullable Function<Long, String> customPrefixProvider;
    private @Nullable Function<Long, Long> commandChannelProvider;
    private long[] serverBlacklist;
    private boolean ignoreBotOwnerPermissions;
    private boolean respondToUnknownCommand;

    public CommandHandler(Discord api) {
        this(api, false);
    }

    public CommandHandler(Discord api, boolean handleMessageEdit) {
        this.api = api;

        prefixes = new String[]{"!"};
        autoDeleteResponseOnCommandDeletion = true;
        customPrefixProvider = null;
        serverBlacklist = new long[0];
        ignoreBotOwnerPermissions = false;
        respondToUnknownCommand = false;

        api.attachListener((MessageSentListener) this::handleMessageCreate);
        if (handleMessageEdit)
            api.attachListener((MessageEditListener) this::handleMessageEdit);
        api.attachListener((MessageDeleteListener) this::handleMessageDelete);
    }

    @Override
    public CommandHandler registerCommandTarget(Object target) {
        Class<?> klasse = target.getClass();

        if (Class.class.isAssignableFrom(klasse)) {
            // register a static class
            Method[] methods = Stream.of(klasse.getMethods())
                    .filter(method -> method.isAnnotationPresent(Command.class))
                    .filter(method -> isStatic(method.getModifiers()))
                    .toArray(Method[]::new);

            extractCommandRep(null, methods);

            return this;
        } else if (Method.class.isAssignableFrom(klasse)) {
            // register a single method
            Method method = (Method) target;

            if (!isStatic(method.getModifiers()))
                logger.at(Level.WARNING).log("Could not register non-static method: %s", method.toGenericString());
            else extractCommandRep(null, method);

            return this;
        } else {
            // register an object
            Method[] methods = Stream.of(klasse.getMethods())
                    .filter(method -> method.isAnnotationPresent(Command.class))
                    .filter(method -> !isStatic(method.getModifiers()))
                    .toArray(Method[]::new);

            extractCommandRep(target, methods);

            return this;
        }
    }

    @Override
    public CommandHandler unregisterCommandTarget(Object target) {
        Class<?> klasse = target.getClass();
        Collection<String> remove = new ArrayList<>();

        if (Class.class.isAssignableFrom(klasse)) {
            // unregister a static class
            Stream.of(klasse.getMethods())
                    .filter(method -> method.isAnnotationPresent(Command.class))
                    .filter(method -> isStatic(method.getModifiers()))
                    .flatMap(method -> commands.entrySet()
                            .stream()
                            .filter(entry -> entry.getValue().method.equals(method)))
                    .map(Map.Entry::getKey)
                    .forEach(remove::add);
        } else if (Method.class.isAssignableFrom(klasse)) {
            // unregister a single method
            commands.entrySet()
                    .stream()
                    .filter(entry -> entry.getValue().method.equals(target))
                    .map(Map.Entry::getKey)
                    .forEach(remove::add);
        } else if (Command.class.isAssignableFrom(klasse)) {
            // TODO unregister a single method from an annotation
        } else if (CommandGroup.class.isAssignableFrom(klasse)) {
            // TODO unregister a static class or a single method from an annotation
        } else if (CommandRepresentation.class.isAssignableFrom(klasse)) {
            // unregister a specific command representation
            commands.entrySet()
                    .stream()
                    .filter(entry -> entry.getValue().equals(target))
                    .map(Map.Entry::getKey)
                    .forEach(remove::add);
        } else {
            // unregister an object
            commands.entrySet()
                    .stream()
                    .filter(entry -> Objects.equals(entry.getValue().invocationTarget, target))
                    .map(Map.Entry::getKey)
                    .forEach(remove::add);
        }

        for (String s : remove)
            if (commands.remove(s) != null)
                logger.at(Level.INFO).log("Successfully unregistered command: " + s);
            else logger.at(Level.WARNING).log("Could not unregister command: " + s);

        return this;
    }

    public Set<CommandRepresentation> getCommands() {
        return new HashSet<>(commands.values());
    }

    public void useDefaultHelp(@Nullable Supplier<Embed> embedSupplier) {
        this.embedSupplier = (embedSupplier == null ? DefaultEmbedFactory.INSTANCE : embedSupplier);
        registerCommands(this);
    }

    public long[] getServerBlacklist() {
        return serverBlacklist;
    }

    public void setServerBlacklist(long... serverIds) {
        this.serverBlacklist = serverIds;
    }

    @CommandGroup(name = "Basic Commands", description = "All commands for basic interaction with the bot")
    @Command(
            aliases = "help",
            usage = "help [command]",
            description = "Shows a list of commands and what they do.",
            ordinal = Integer.MIN_VALUE
    )
    public Object defaultHelpCommand(TextChannel channel, String[] args) {
        if (args.length == 0) {
            if (getCommands().stream()
                    .filter(cmd -> cmd.showInHelpCommand)
                    .filter(cmd -> !Objects.equals(cmd.groupName, "Basic Commands"))
                    .allMatch(cmd -> cmd.groupName == null)) {
                PagedEmbed embed = new PagedEmbed(channel, embedSupplier);
                getCommands().stream()
                        .filter(cmd -> cmd.showInHelpCommand)
                        .sorted(Comparator.<CommandRepresentation>comparingInt(cmd -> cmd.groupOrdinal)
                                .thenComparingInt(rep -> rep.ordinal))
                        .forEachOrdered(cmd -> embed.addField("__" + cmd.aliases[0] + "__: _" + prefixes[0]
                                + cmd.usage + "_", cmd.description));
                return embed;
            } else {
                CategorizedEmbed embed = new CategorizedEmbed(channel);

                getCommands().stream()
                        .filter(cmd -> cmd.showInHelpCommand)
                        .sorted(Comparator.<CommandRepresentation>comparingInt(cmd -> cmd.groupOrdinal)
                                .thenComparingInt(rep -> rep.ordinal))
                        .filter(cmd -> embed.getCategories()
                                .stream()
                                .noneMatch(cat -> cmd.groupName != null
                                        ? cat.getName().equals(cmd.groupName)
                                        : cat.getName().equals("Other commands")))
                        .forEachOrdered(cmd -> {
                            if (cmd.groupName != null)
                                embed.addCategory(cmd.groupName, cmd.groupDescription != null ? cmd.groupDescription : "");
                            else embed.addCategory("Other commands", "Ungrouped commands");
                        });

                getCommands().stream()
                        .filter(cmd -> cmd.showInHelpCommand)
                        .sorted(Comparator.<CommandRepresentation>comparingInt(cmd -> cmd.groupOrdinal)
                                .thenComparingInt(rep -> rep.ordinal))
                        .forEach(cmd -> embed.getCategories()
                                .stream()
                                .filter(cat -> cmd.groupName != null
                                        ? cat.getName().equals(cmd.groupName)
                                        : cat.getName().equals("Other commands"))
                                .findFirst()
                                .ifPresent(cat -> cat.addField("__" + cmd.aliases[0] + "__: _" + prefixes[0]
                                        + cmd.usage + "_", cmd.description)));

                return embed;
            }
        } else {
            Embed embed = embedSupplier.get();
            Optional<CommandRepresentation> command = getCommands().stream()
                    .filter(cmd -> {
                        for (String alias : cmd.aliases)
                            if (alias.equalsIgnoreCase(args[0]))
                                return true;
                        return false;
                    }).findAny();

            if (command.isPresent()) {
                CommandRepresentation cmd = command.get();
                embed.addField("__" + cmd.aliases[0] + "__: _" + prefixes[0] + cmd.usage + "_", cmd.description);
            } else embed.addField(
                    "__Unknown Command__: _" + args[0] + "_",
                    "Type _\"" + prefixes[0] + "help\"_ for a list of commands."
            );

            return embed;
        }

    }

    @Override
    public CommandHandler withCommandChannelProvider(@NotNull Function<Long, Long> commandChannelProvider) {
        this.commandChannelProvider = commandChannelProvider;
        return this;
    }

    @Override
    public Optional<Function<Long, Long>> getCommandChannelProvider() {
        return Optional.ofNullable(commandChannelProvider);
    }

    @Override
    public CommandHandler withCustomPrefixProvider(@NotNull Function<Long, String> customPrefixProvider) {
        this.customPrefixProvider = customPrefixProvider;
        return this;
    }

    @Override
    public Optional<Function<Long, String>> getCustomPrefixProvider() {
        return Optional.ofNullable(customPrefixProvider);
    }

    @Override public CommandHandler ignoreBotOwnerPermissions(boolean status) {
        this.ignoreBotOwnerPermissions = status;
        return this;
    }

    @Override public boolean doesIgnoreBotOwnerPermissions() {
        return ignoreBotOwnerPermissions;
    }

    @Override
    public CommandHandler withUnknownCommandResponseStatus(boolean status) {
        this.respondToUnknownCommand = status;
        return this;
    }

    @Override
    public boolean doesRespondToUnknownCommands() {
        return respondToUnknownCommand;
    }

    private void extractCommandRep(@Nullable Object invocationTarget, Method... methods) {
        for (Method method : methods) {
            Command cmd = method.getAnnotation(Command.class);
            if (cmd == null) continue;

            CommandGroup group;
            if ((group = method.getAnnotation(CommandGroup.class)) == null) {
                Class<?> declaring = method.getDeclaringClass();
                if ((group = declaring.getAnnotation(CommandGroup.class)) == null) {
                    Class<?>[] supers = new Class[declaring.getInterfaces().length
                            + (declaring.getSuperclass() == Object.class ? 0 : 1)];
                    for (int i = 0; i < declaring.getInterfaces().length; i++) supers[i] = declaring.getInterfaces()[i];
                    if (declaring.getSuperclass() != Object.class)
                        supers[supers.length - 1] = declaring.getSuperclass();

                    int i = 0;
                    while (i < supers.length && (group = supers[i].getAnnotation(CommandGroup.class)) == null)
                        i++;
                }
            }

            if (!isStatic(method.getModifiers()) && Objects.isNull(invocationTarget))
                throw new IllegalArgumentException("Invocation Target cannot be null on non-static methods!");
            if (Modifier.isAbstract(method.getModifiers()))
                throw new AbstractMethodError("Command annotated method cannot be abstract!");

            boolean hasErrored = false;
            if (!cmd.enableServerChat()
                    && Util.arrayContains(cmd.requiredDiscordPermissions(), Permission.SEND_MESSAGES)) {
                logger.at(Level.SEVERE).log("Command " + method.getName() + "(" + Arrays.stream(method.getParameterTypes())
                        .map(Class::getSimpleName)
                        .collect(Collectors.joining(", ")) + ")"
                        + ": Conflicting command properties; private-only commands cannot require permissions!");
                hasErrored = true;
            }
            if (!cmd.enableServerChat() && !cmd.enableServerChat()) {
                logger.at(Level.SEVERE).log("Command " + method.getName() + "(" + Arrays.stream(method.getParameterTypes())
                        .map(Class::getSimpleName)
                        .collect(Collectors.joining(", ")) + ")"
                        + ": Conflicting command properties; command cannot disallow both private and server chat!");
                hasErrored = true;
            }

            if (hasErrored) continue;

            CommandRepresentation commandRep = new CommandRepresentation(method, cmd, group, invocationTarget);
            if (cmd.aliases().length > 0)
                for (String alias : cmd.aliases()) commands.put(alias, commandRep);
            else commands.put(method.getName(), commandRep);
            if (group == null)
                logger.at(Level.INFO).log("Command " + (cmd.aliases().length == 0 ? method.getName() : cmd.aliases()[0])
                        + " was registered without a CommandGroup annotation!");
        }
    }

    private void handleMessageCreate(MessageSentEvent event) {
        if (isBlacklisted(event)) return;

        Params params = new Params(
                api,
                event,
                null,
                event.getTriggeringGuild(),
                event.getTriggeringChannel(),
                event.getTriggeringMessage(),
                event.getTriggeringMessage().getAuthor()
        );

        handleCommand(params.message, event.getTriggeringChannel(), params);
    }

    private void handleMessageEdit(MessageEditEvent event) {
        if (isBlacklisted(event)) return;

        Params params = new Params(
                api,
                null,
                event,
                event.getTriggeringGuild(),
                event.getTriggeringChannel(),
                event.getTriggeringMessage(),
                event.getTriggeringMessage().getAuthor()
        );

        handleCommand(params.message, event.getTriggeringChannel(), params);
    }

    private void handleMessageDelete(MessageDeleteEvent event) {
        if (isBlacklisted(event)) return;

        if (autoDeleteResponseOnCommandDeletion) {
            long[] ids = responseMap.get(event.getTriggeringMessage().getID());
            if (ids == null) return;
            Message.request(api, api.getChannelByID(ids[1])
                            .map(Snowflake::getID)
                            .orElseThrow(AssertionError::new), ids[0])
                    .thenCompose(Message::delete)
                    .exceptionally(exceptionLogger());
        }
    }

    private <E extends MessageEvent & WrappedGuildEvent> boolean isBlacklisted(E event) {
        if (event.wrapTriggeringGuild().isEmpty()) return false;

        long id = event.getTriggeringGuild().getID();

        for (long blacklisted : serverBlacklist) if (id == blacklisted) return false;

        return false;
    }

    private void handleCommand(final Message message, final TextChannel channel, final Params commandParams) {
        if (commandChannelProvider != null && !message.isPrivate()) {
            long serverId = channel.asGuildChannel()
                    .map(GuildChannel::getGuild)
                    .map(Snowflake::getID)
                    .orElseThrow(AssertionError::new);

            if (!api.getChannelByID(commandChannelProvider.apply(serverId))
                    .map(channel::equals)
                    .orElse(true))
                return;
        }

        String usedPrefix = extractUsedPrefix(message);

        // if no prefix was used, stop handling
        if (usedPrefix == null) return;

        String content = message.getContent();

        CommandRepresentation cmd;
        String[] split = splitContent(content);
        final String[] commandName = new String[1];
        String[] args;
        if (usedPrefix.matches("^(.*\\s.*)+$")) {
            cmd = commands.entrySet()
                    .stream()
                    .filter(entry -> entry.getKey()
                            .toLowerCase()
                            .equals((commandName[0] = split[1]).substring(usedPrefix.length()).toLowerCase()))
                    .findFirst()
                    .map(Map.Entry::getValue)
                    .orElse(null);
            args = new String[split.length - 2];
            arraycopy(split, 2, args, 0, args.length);
        } else {
            cmd = commands.entrySet()
                    .stream()
                    .filter(entry -> entry.getKey()
                            .toLowerCase()
                            .equals((commandName[0] = split[0]).substring(usedPrefix.length()).toLowerCase()))
                    .findFirst()
                    .map(Map.Entry::getValue)
                    .orElse(null);
            args = new String[split.length - 1];
            arraycopy(split, 1, args, 0, args.length);
        }

        List<String> problems = new ArrayList<>();

        if (cmd == null) {
            if (respondToUnknownCommand)
                problems.add("Unknown command: " + usedPrefix + commandName[0]);
            else return;
        }

        commandParams.command = cmd;
        commandParams.args = args;

        if (cmd.useTypingIndicator) 
            channel.triggerTypingIndicator();

        if (message.isPrivate() && !cmd.enablePrivateChat)
            problems.add("This command can only be run in a server channel!");
        else if (!message.isPrivate() && !cmd.enableServerChat)
            problems.add("This command can only be run in a private channel!");

        Permission[] perms = new Permission[cmd.requiredDiscordPermissions.length];
        perms[0] = Permission.ADMINISTRATOR;
        System.arraycopy(cmd.requiredDiscordPermissions, 0, perms, 1, cmd.requiredDiscordPermissions.length);
        if (!(ignoreBotOwnerPermissions && message.getAuthor().isBotOwner())
                && !message.getAuthor()
                .castAuthorToUser()
                .map(usr -> message.getChannel()
                        .asGuildTextChannel()
                        .map(stc -> stc.hasAnyPermission(usr, perms))
                        .orElse(true))
                .orElse(false))
            problems.add("You are missing " + (perms.length == 2
                    ? "the required permission: " + perms[1].name() + "!"
                    : "one of the required permissions: " + Arrays.toString(perms)));

        int reqArgs = cmd.minimumArguments;
        if (commandParams.args.length < reqArgs) problems.add("This command requires at least "
                + reqArgs + " argument" + (reqArgs == 1 ? "" : "s") + "!");

        int maxArgs = cmd.maximumArguments;
        if (commandParams.args.length > maxArgs) problems.add("This command allows a maximum of "
                + maxArgs + " argument" + (maxArgs == 1 ? "" : "s") + "!");

        int reqChlMent = cmd.requiredChannelMentions;
        if (message.getMentionedChannels().size() < reqChlMent) problems.add("This command requires at least "
                + reqChlMent + " channel mention" + (reqChlMent == 1 ? "" : "s") + "!");

        int reqUsrMent = cmd.requiredUserMentions;
        if (message.getMentionedUsers().size() < reqUsrMent) problems.add("This command requires at least "
                + reqUsrMent + " user mention" + (reqUsrMent == 1 ? "" : "s") + "!");

        int reqRleMent = cmd.requiredRoleMentions;
        if (message.getMentionedRoles().size() < reqRleMent) problems.add("This command requires at least "
                + reqRleMent + " role mention" + (reqRleMent == 1 ? "" : "s") + "!");

        if (cmd.runInNSFWChannelOnly
                && !channel.asGuildTextChannel().map(GuildTextChannel::isNSFW).orElse(true))
            problems.add("This command can only run in an NSFW marked channel!");

        if (problems.size() > 0) {
            applyResponseDeletion(message.getID(), channel.composeMessage()
                    .setEmbed(DefaultEmbedFactory.create()
                    .setColor(Color.RED)
                    .setDescription(String.join("\n", problems)))
                    .send()
                    .exceptionally(exceptionLogger()));
            return;
        }

        if (cmd.async) api.getCommonThreadPool()
                .submit(() -> doInvoke(cmd, commandParams, channel, message));
        else doInvoke(cmd, commandParams, channel, message);
    }

    private @Nullable String extractUsedPrefix(final Message message) {
        String content = message.getContent();
        int usedPrefix = -1;

        // gather all possible prefixes
        String[] prefs = new String[prefixes.length
                + (useBotMentionAsPrefix ? 2 : 0)
                + (customPrefixProvider != null ? 1 : 0)];
        arraycopy(prefixes, 0, prefs, 0, prefixes.length);
        if (useBotMentionAsPrefix) {
            prefs[prefixes.length] = api.getYourself().getMentionTag() + " ";
            prefs[prefixes.length + 1] = api.getYourself().getNicknameMentionTag() + " ";
        }
        if (customPrefixProvider != null) message.getGuild()
                .map(Snowflake::getID)
                .map(customPrefixProvider)
                .ifPresent(val -> prefs[prefs.length - 1] = val);

        for (int i = 0; i < prefs.length; i++)
            if (content.toLowerCase().indexOf(prefs[i]) == 0) {
                usedPrefix = i;
                break;
            }

        return usedPrefix == -1 ? null : prefs[usedPrefix];
    }

    private String[] splitContent(String content) {
        List<String> yields = new ArrayList<>();
        yields.add("");

        boolean inString = false;
        int i = 0, y = 0, s = -1;
        char c = 0, p;

        while (i < content.length()) {
            p = c;
            c = content.charAt(i);

            switch (c) {
                case '"':
                    // if not in string & starter is not escaped
                    if (!inString && p != '\\') {
                        // if string starts after a space
                        if (p == ' ') {
                            // start string
                            yields.add("");
                            s = y++;
                            inString = true;
                        } else {
                            // escape "
                            yields.set(y, yields.get(y) + c);
                        }
                        // if in string & ender is not escaped
                    } else if (inString && p != '\\') {
                        // if there are more chars
                        if (content.length() < i + 1) {
                            // if next char is space
                            if (content.length() < i + 1 && content.charAt(i + 1) == ' ') {
                                // end string
                                yields.add("");
                                s = y++;
                                inString = false;
                            } else {
                                // escape "
                                yields.set(y, yields.get(y) + c);
                            }
                        } else {
                            // end string
                            inString = false;
                        }
                        // if " was escaped
                    } else {
                        // append to string
                        yields.set(y, yields.get(y) + c);
                    }
                    break;
                case ' ':
                    if (inString) {
                        // append to string
                        yields.set(y, yields.get(y) + c);
                    } else {
                        // start new item
                        yields.add("");
                        s = y++;
                    }
                    break;
                case '\\':
                    // never include escaping character
                    break;
                default:
                    // append to string
                    yields.set(y, yields.get(y) + c);
                    break;
            }

            i++;
        }

        if (inString) { // if still in string -> join split string together
            int prevSize = yields.size();
            new ArrayList<>(yields)
                    .stream()
                    .skip(s)
                    .flatMap(str -> Arrays.stream(str.split(" ")))
                    .forEachOrdered(yields::add);
            if (prevSize >= s + 1) yields.subList(s + 1, prevSize + 1).clear();
            yields.set(s + 1, '"' + yields.get(s + 1));
        }

        yields.removeIf(String::isEmpty);

        return yields.toArray(new String[0]);
    }

    private void doInvoke(CommandRepresentation commandRep, Params commandParams, TextChannel channel, Message message) {
        Object reply;
        try {
            reply = invoke(commandRep.method, commandParams, commandRep.invocationTarget);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Cannot access command method!", e);
        } catch (InvocationTargetException e) {
            new InfoReaction(message,
                    Emoji.unicode("⚠"),
                    "Command threw an exception: [" + e.getCause().getClass().getSimpleName() + "] \n```\n" + e.getCause().getMessage() + "\n```",
                    1, TimeUnit.MINUTES,
                    () -> DefaultEmbedFactory.create().setColor(Color.RED))
                    .build();
            logger.at(Level.SEVERE)
                    .withCause(e.getCause())
                    .log();
            return;
        }

        if (reply != null) {
            CompletableFuture<Message> msgFut = null;

            if (reply instanceof Embed) msgFut = channel.composeMessage()
                    .setEmbed((Embed) reply)
                    .send();
            else if (reply instanceof Message.Composer) msgFut = ((Message.Composer) reply).send(channel);
            else if (reply instanceof InformationMessage) ((InformationMessage) reply).refresh();
            else if (reply instanceof PagedEmbed) msgFut = ((PagedEmbed) reply).build();
            else if (reply instanceof PagedMessage) ((PagedMessage) reply).refresh();
            else if (reply instanceof RefreshableMessage) ((RefreshableMessage) reply).refresh();
            else if (reply instanceof CategorizedEmbed) msgFut = ((CategorizedEmbed) reply).build();
            else if (reply instanceof InfoReaction) ((InfoReaction) reply).build();
            else if (commandRep.convertStringResultsToEmbed && reply instanceof String)
                msgFut = channel.composeMessage()
                        .setEmbed(DefaultEmbedFactory.create().setDescription((String) reply))
                        .send();
            else msgFut = channel.composeMessage()
                        .setText(String.valueOf(reply))
                        .send();

            if (msgFut != null)
                applyResponseDeletion(message.getID(), msgFut.exceptionally(exceptionLogger()));
        }
    }

    private Object invoke(Method method, Params param, @Nullable Object invocationTarget)
            throws InvocationTargetException, IllegalAccessException {
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] args = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> klasse = parameterTypes[i];

            if (CommandRepresentation.class.isAssignableFrom(klasse))
                args[i] = param.command;
            else if (Discord.class.isAssignableFrom(klasse))
                args[i] = param.discord;
            else if (MessageSentEvent.class.isAssignableFrom(klasse))
                args[i] = param.createEvent;
            else if (MessageEditEvent.class.isAssignableFrom(klasse))
                args[i] = param.editEvent;
            else if (Guild.class.isAssignableFrom(klasse))
                args[i] = param.server;
            else if (Boolean.class.isAssignableFrom(klasse))
                args[i] = param.message.isPrivate();
            else if (TextChannel.class.isAssignableFrom(klasse)) {
                if (GuildTextChannel.class.isAssignableFrom(klasse))
                    args[i] = param.textChannel.asGuildTextChannel().orElse(null);
                else if (PrivateChannel.class.isAssignableFrom(klasse))
                    args[i] = param.textChannel.asPrivateTextChannel().orElse(null);
                else args[i] = param.textChannel;
            } else if (Message.class.isAssignableFrom(klasse))
                args[i] = param.message;
            else if (User.class.isAssignableFrom(klasse))
                args[i] = param.author.castAuthorToUser().orElse(null);
            else if (MessageAuthor.class.isAssignableFrom(klasse))
                args[i] = param.author;
            else if (String[].class.isAssignableFrom(klasse))
                args[i] = param.args;
            else if (Command.Parameters.class.isAssignableFrom(klasse))
                args[i] = param;
            else args[i] = null;
        }

        return method.invoke(invocationTarget, args);
    }

    private void applyResponseDeletion(long cmdMsgId, CompletableFuture<Message> message) {
        message.thenAcceptAsync(msg -> {
            if (autoDeleteResponseOnCommandDeletion)
                responseMap.put(cmdMsgId, new long[]{msg.getID(), msg.getChannel().getID()});
        });
    }

    private class Params implements Command.Parameters {
        private final Discord discord;
        @Nullable private final MessageSentEvent createEvent;
        @Nullable private final MessageEditEvent editEvent;
        @Nullable private final Guild server;
        private final TextChannel textChannel;
        private final Message message;
        private final MessageAuthor author;
        private CommandRepresentation command;
        private String[] args;

        private Params(
                Discord discord,
                @Nullable MessageSentEvent createEvent,
                @Nullable MessageEditEvent editEvent,
                @Nullable Guild server,
                TextChannel textChannel,
                Message message,
                @Nullable MessageAuthor author
        ) {
            this.command = command;
            this.discord = discord;
            this.createEvent = createEvent;
            this.editEvent = editEvent;
            this.server = server;
            this.textChannel = textChannel;
            this.message = message;
            this.author = author;
        }

        @Override
        public CommandRepresentation getCommand() {
            return command;
        }

        @Override
        public Discord getDiscord() {
            return discord;
        }

        @Override
        public Optional<MessageSentEvent> getMessageSentEvent() {
            return Optional.ofNullable(createEvent);
        }

        @Override
        public Optional<MessageEditEvent> getMessageEditEvent() {
            return Optional.ofNullable(editEvent);
        }

        @Override
        public Optional<Guild> getServer() {
            return Optional.ofNullable(server);
        }

        @Override
        public TextChannel getTextChannel() {
            return textChannel;
        }

        @Override
        public Message getCommandMessage() {
            return message;
        }

        @Override
        public Optional<MessageAuthor> getCommandExecutor() {
            return Optional.ofNullable(author);
        }

        @Override
        public String[] getArguments() {
            return args;
        }
    }
}
