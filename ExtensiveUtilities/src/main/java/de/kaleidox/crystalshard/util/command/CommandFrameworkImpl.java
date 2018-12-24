package de.kaleidox.crystalshard.util.command;

import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;

import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.entity.channel.PrivateTextChannel;
import de.kaleidox.crystalshard.api.entity.channel.ServerTextChannel;
import de.kaleidox.crystalshard.api.entity.channel.TextChannel;
import de.kaleidox.crystalshard.api.entity.message.Message;
import de.kaleidox.crystalshard.api.entity.message.embed.Embed;
import de.kaleidox.crystalshard.api.entity.permission.Permission;
import de.kaleidox.crystalshard.api.entity.server.Server;
import de.kaleidox.crystalshard.api.entity.user.Author;
import de.kaleidox.crystalshard.api.entity.user.AuthorUser;
import de.kaleidox.crystalshard.api.entity.user.Self;
import de.kaleidox.crystalshard.api.handling.event.message.generic.MessageCreateEvent;
import de.kaleidox.crystalshard.api.handling.listener.message.generic.MessageCreateListener;
import de.kaleidox.crystalshard.logging.Logger;
import de.kaleidox.crystalshard.util.embeds.PagedEmbed;
import de.kaleidox.util.functional.Switch;
import de.kaleidox.util.helpers.ListHelper;
import de.kaleidox.util.helpers.SetHelper;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class represents the CommandFramework. Commands can be created using the {@link Command} annotation.
 */
public class CommandFrameworkImpl implements CommandFramework {
    private final static Logger logger = new Logger(CommandFrameworkImpl.class);
    private final AtomicBoolean enabled = new AtomicBoolean(false);
    private final Discord discord;
    private final String prefix;
    private final List<Instance> commands;
    private final List<TextChannel> ignored;

    /**
     * Creates a new instance. Requires a prefix.
     *
     * @param discord                  The discord object to attach to.
     * @param prefix                   The prefix for commands. May end with a space.
     * @param enableBuiltInHelpCommand Whether the default help command should be registered.
     */
    public CommandFrameworkImpl(Discord discord, String prefix, boolean enableBuiltInHelpCommand) {
        this.discord = discord;
        this.prefix = prefix;
        this.commands = new ArrayList<>();
        this.ignored = new ArrayList<>();

        enabled.set(true);

        if (!enableBuiltInHelpCommand) {
            Method helpMethod = null;
            for (Method method : CommandFrameworkImpl.class.getMethods()) {
                if (method.getName().equals("defaultHelp")) {
                    helpMethod = method;
                    break;
                }
            }
            assert helpMethod != null;
            unregisterCommand(helpMethod);
        }
        commandRegistry();
        discord.attachListener((MessageCreateListener) this::handle);
    }

    private void commandRegistry() {
        Package[] packages = ClassLoader.getSystemClassLoader().getDefinedPackages();
        String[] packageNames = new String[packages.length];
        for (int i = 0; i < packages.length; i++) packageNames[i] = packages[i].getName();
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .addScanners(new MethodAnnotationsScanner())
                .forPackages(packageNames));
        reflections.getMethodsAnnotatedWith(Command.class).forEach(this::registerCommands);
    }

    /**
     * Tries to register all {@link Command} annotated methods of the given class as commands.
     *
     * @param commandClass The class to register command methods in.
     * @throws IllegalArgumentException  If a duplicate alias was found.
     * @throws InvalidParameterException If a private-only marked method has {@link Server} as a parameter.
     * @throws IllegalStateException     If a Command-Method is not {@link Modifier#STATIC}.
     */
    @Override
    public void registerCommands(Class commandClass) throws IllegalArgumentException, IllegalStateException {
        Stream.of(commandClass.getMethods())
                .filter(method -> method.isAnnotationPresent(Command.class))
                .forEach(this::registerCommandMethod);
    }

    /**
     * Returns a list of registered Command annotations. This can be useful for implementing a custom help command.
     *
     * @return All registered Command annotations.
     */
    @Override
    public List<Command> getCommands() {
        return commands.stream()
                .map(inst -> inst.annotation)
                .collect(Collectors.toList());
    }

    /**
     * Gets the Discord object that this framework is attached to.
     *
     * @return The discord object.
     */
    @Override
    public Discord getDiscord() {
        return discord;
    }

    /**
     * Enables listening for commands globally.
     */
    @Override
    public void enable() {
        enabled.set(true);
    }

    /**
     * Disables listening for commands globally.
     */
    @Override
    public void disable() {
        enabled.set(false);
    }

    /**
     * Ignores a {@link TextChannel} from executing commands.
     *
     * @param object The channel to ignore.
     */
    @Override
    public void ignore(TextChannel object) {
        ignored.add(object);
    }

    /**
     * Tries to remove a {@link TextChannel} from the ignored-list.
     *
     * @param object The object to remove from the ignore list.
     * @return Whether the {@link TextChannel} could be removed from the ignored list.
     */
    @Override
    public boolean unignore(TextChannel object) {
        return ignored.stream()
                .filter(object::equals)
                .findAny()
                .map(ignored::remove)
                .orElse(false);
    }

    /**
     * Registers a {@link Command} annotated method as a command.
     *
     * @param commandMethod The method to register.
     * @throws IllegalArgumentException  If a duplicate alias was found.
     * @throws InvalidParameterException If a private-only marked method has {@link Server} as a parameter.
     * @throws IllegalStateException     The Command-Method is not {@link Modifier#STATIC}.
     */
    @Override
    public void registerCommands(Method commandMethod) throws IllegalArgumentException, IllegalStateException {
        registerCommandMethod(commandMethod);
    }

    /**
     * Tries to unregister a {@link Command} annotated method.
     *
     * @param commandMethod The method to unregister.
     * @return Whether the command could be unregistered.
     */
    @Override
    public boolean unregisterCommand(Method commandMethod) {
        return commands.stream()
                .filter(instance -> instance.method == commandMethod)
                .findAny()
                .map(commands::remove)
                .orElse(false);
    }

    private void handle(MessageCreateEvent event) {
        if (enabled.get()) {
            if (!ignored.contains(event.getChannel()
                    .toTextChannel()
                    .orElseThrow(AssertionError::new))) {
                commands.stream()
                        .filter(instance -> checkAlias(instance, event.getMessageContent()))
                        .filter(instance -> checkProperties(instance.annotation, event))
                        .map(instance -> instance.method)
                        .forEach(method -> runWithDynamicParams(method, event));
            }
        }
    }

    private void runWithDynamicParams(Method method, MessageCreateEvent event) {
        final Command annotation = method.getAnnotation(Command.class);
        Discord discord = event.getDiscord();
        Server server = event.getServer()
                .orElse(null);
        TextChannel channel = event.getChannel()
                .toTextChannel()
                .orElseThrow(AssertionError::new);
        Message message = event.getMessage();
        Author author = event.getMessageAuthor();
        String content = event.getMessageContent();

        Class<?>[] parameterTypes = method.getParameterTypes();
        final Object[] finalParam = new Object[parameterTypes.length];
        final int[] ref = new int[1];
        @SuppressWarnings("unchecked") Switch<Class> swtc = new Switch<Class>(Class::isAssignableFrom)
                .addCase(MessageCreateEvent.class, type -> finalParam[ref[0]] = event)
                .addCase(Discord.class, type -> finalParam[ref[0]] = discord)
                .addCase(Server.class, type -> finalParam[ref[0]] = server)
                .addCase(TextChannel.class, type -> finalParam[ref[0]] = channel)
                .addCase(Message.class, type -> finalParam[ref[0]] = message)
                .addCase(Author.class, type -> finalParam[ref[0]] = author)
                .addCase(String.class, type -> finalParam[ref[0]] = content)
                .defaultCase(type -> finalParam[ref[0]] = null)
                // specified sender channels if possible
                .addCase(ServerTextChannel.class, type ->
                        finalParam[ref[0]] = channel.toServerTextChannel().orElse(null))
                .addCase(PrivateTextChannel.class, type ->
                        finalParam[ref[0]] = channel.toPrivateTextChannel().orElse(null));
        for (ref[0] = 0; ref[0] < parameterTypes.length; ref[0]++) swtc.test(parameterTypes[ref[0]]);

        discord.getThreadPool()
                .execute(() -> {
                    try {
                        method.invoke(this, finalParam);
                    } catch (Exception e) {
                        logger.exception(e, "Exception invoking command Method: " + method.toGenericString());
                    }
                });
    }

    private boolean checkProperties(Command annotation, MessageCreateEvent event) {
        int criteria = 0;
        Permission permission = annotation.requiredDiscordPermission();
        boolean serverChat = annotation.enableServerChat();
        boolean privateChat = annotation.enablePrivateChat();
        int channelMentions = annotation.requireChannelMentions();
        int userMentions = annotation.requireUserMentions();
        int roleMentions = annotation.requireRoleMentions();

        TextChannel channel = event.getChannel()
                .toTextChannel()
                .orElseThrow(AssertionError::new);
        Optional<AuthorUser> authorUserOpt = event.getMessageAuthorUser();
        Message message = event.getMessage();

        if (authorUserOpt.isPresent()) {
            if (channel.hasPermission(authorUserOpt.get(), permission)) criteria++;
        } else criteria++;

        if (serverChat == !channel.isPrivate()) criteria++;
        else if (privateChat == channel.isPrivate()) criteria++;

        if (message.getChannelMentions()
                .size() >= channelMentions) criteria++;
        if (message.getUserMentions()
                .size() >= userMentions) criteria++;
        if (message.getRoleMentions()
                .size() >= roleMentions) criteria++;

        return criteria == 6;
    }

    private boolean checkAlias(Instance instance, String messageContent) {
        if (messageContent.indexOf(prefix) == 0) {
            List<String> split = ListHelper.of(messageContent.split(" "));
            boolean prefixHasSpace = (prefix.charAt(prefix.length() - 1) == ' ');
            for (String alias : instance.annotation.aliases()) {
                Objects.requireNonNull(alias);
                if (split.get(prefixHasSpace ? 1 : 0)
                        .equalsIgnoreCase((prefixHasSpace ? "" : prefix) + alias)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void registerCommandMethod(Method method) throws IllegalArgumentException, IllegalStateException {
        if (!method.isAnnotationPresent(Command.class)) throw new IllegalArgumentException(
                "Method " + method.toGenericString() + " does not have annotation " + Command.class + ".");
        Command commandAnnot = method.getAnnotationsByType(Command.class)[0];
        if (!Modifier.isStatic(method.getModifiers()))
            throw new IllegalStateException("The command methods must be static.");
        if (!commandAnnot.enableServerChat() && SetHelper.of(HashSet.class, method.getParameterTypes())
                .contains(Server.class)) logger.exception(new InvalidParameterException(
                "Command " + method.toGenericString() + " is " + "annotated to not run on " + "Servers, yet expects a Server-Parameter. It will " +
                        "only recieve " + "null."), "Error in Command method body for: " + method.toGenericString());
        if (hasAliasesRegistered(commandAnnot.aliases())) logger.exception(new IllegalArgumentException(
                        "A command with one of the aliases " + Arrays.toString(commandAnnot.aliases()) + " is registered already!"),
                "Error in Command aliases for: " + method.toGenericString());
        Instance instance = new Instance(method, commandAnnot);
        commands.add(instance);
    }

    private boolean hasAliasesRegistered(String[] aliases) {
        return commands.stream()
                .flatMap(cmd -> Stream.of(cmd.annotation.aliases()))
                .anyMatch(alias -> ListHelper.of(aliases)
                        .contains(alias));
    }

    /**
     * This method describes the default help command. This command is being enabled by passing {@code TRUE} as third argument when initializing the Command
     * Framework.
     *
     * @param discord A discord.
     * @param server  A server.
     * @param author  An author.
     * @param channel A channel.
     */
    @Command(aliases = "help", description = "Shows a list of all commands.")
    public static void defaultHelp(Discord discord, Server server, Author author, TextChannel channel) {
        final CommandFrameworkImpl framework = (CommandFrameworkImpl) discord.getUtilities()
                .getCommandFramework();
        final Self self = discord.getSelf();
        final Embed.Builder builder = PagedEmbed.builder(discord);

        self.getAvatarUrl()
                .map(URL::toExternalForm)
                .ifPresent(builder::setThumbnail);
        builder.setTitle(self.getDisplayName(server) + " Commands");

        framework.commands.stream()
                .map(inst -> inst.annotation)
                .forEachOrdered(annotation -> {
                    if (annotation.shownInDefaultHelp()) {
                        StringBuilder sb = new StringBuilder();
                        String description = annotation.description();

                        sb.append(description)
                                .append("\nWorks in:\n")
                                .append(annotation.enablePrivateChat() ? "🔒 Private Chat\n" : "")
                                .append(annotation.enableServerChat() ? "🌐 Server Chat\n" : "")
                                .append(annotation.requiredDiscordPermission() == Permission.EMPTY ? "" :
                                        String.format("Required Discord-Permission: %s\n",
                                                annotation.requiredDiscordPermission().toString()))
                                .append(annotation.requireChannelMentions() == 0 ? "" :
                                        String.format("Required Channel-Mentions: %d\n",
                                                annotation.requireChannelMentions()))
                                .append(annotation.requireUserMentions() == 0 ? "" :
                                        String.format("Required User-Mentions: %d\n", annotation.requireUserMentions()))
                                .append(annotation.requireRoleMentions() == 0 ? "" :
                                        String.format("Required Role mentions: %d\n", annotation.requireRoleMentions())
                                );

                        StringBuilder aliases = new StringBuilder(framework.prefix);
                        Iterator<String> iterator = ListHelper.of(annotation.aliases())
                                .iterator();
                        while (iterator.hasNext()) {
                            String next = iterator.next();
                            aliases.append(next);
                            if (iterator.hasNext()) aliases.append(" | ");
                        }

                        builder.addField(aliases.toString(), sb.toString());
                    }
                });
        builder.send(channel);
    }

    private class Instance {
        private final Method method;
        private final Command annotation;

        private Instance(Method method, Command annotation) {
            this.method = method;
            this.annotation = annotation;
        }
    }
}
