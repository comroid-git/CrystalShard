package de.kaleidox.crystalshard.util.command;

import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.handling.event.message.generic.MessageCreateEvent;
import de.kaleidox.crystalshard.main.handling.listener.message.generic.MessageCreateListener;
import de.kaleidox.crystalshard.main.items.channel.TextChannel;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.items.message.embed.Embed;
import de.kaleidox.crystalshard.main.items.permission.Permission;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.user.Author;
import de.kaleidox.crystalshard.main.items.user.AuthorUser;
import de.kaleidox.crystalshard.main.items.user.Self;
import de.kaleidox.crystalshard.util.discord.messages.PagedEmbed;
import de.kaleidox.crystalshard.logging.Logger;
import de.kaleidox.crystalshard.util.objects.functional.Switch;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class represents the CommandFramework. Commands can be created using the {@link Command} annotation.
 */
public class CommandFramework {
    private final static Logger            logger  = new Logger(CommandFramework.class);
    private final        AtomicBoolean     enabled = new AtomicBoolean(false);
    private final        Discord           discord;
    private final        String            prefix;
    private final        List<Instance>    commands;
    private final        List<TextChannel> ignored;
    
    /**
     * Creates a new instance. Requires a prefix.
     *
     * @param discord                  The discord object to attach to.
     * @param prefix                   The prefix for commands. May end with a space.
     * @param enableBuiltInHelpCommand Whether the default help command should be registered.
     */
    public CommandFramework(Discord discord, String prefix, boolean enableBuiltInHelpCommand) {
        this.discord = discord;
        this.prefix = prefix;
        this.commands = new ArrayList<>();
        this.ignored = new ArrayList<>();
        
        enabled.set(true);
        
        discord.attachListener((MessageCreateListener) this::handle);
        
        if (enableBuiltInHelpCommand) registerCommands(CommandFramework.class);
    }
    
    /**
     * Returns a list of registered Command annotations. This can be useful for implementing a custom help command.
     *
     * @return All registered Command annotations.
     */
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
    public Discord getDiscord() {
        return discord;
    }
    
    /**
     * Enables listening for commands globally.
     */
    public void enable() {
        enabled.set(true);
    }
    
    /**
     * Disables listening for commands globally.
     */
    public void disable() {
        enabled.set(false);
    }
    
    /**
     * Ignores a {@link TextChannel} from executing commands.
     *
     * @param object The channel to ignore.
     */
    public void ignore(TextChannel object) {
        ignored.add(object);
    }
    
    /**
     * Tries to remove a {@link TextChannel} from the ignored-list.
     *
     * @param object The object to remove from the ignore list.
     * @return Whether the {@link TextChannel} could be removed from the ignored list.
     */
    public boolean unignore(TextChannel object) {
        return ignored.stream()
                .filter(object::equals)
                .findAny()
                .map(ignored::remove)
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
        Object[] finalParam = new Object[parameterTypes.length];
        var ref = new Object() {
            int i;
        };
        @SuppressWarnings("unchecked") Switch<Class> classSwitch = new Switch<Class>(Class::isAssignableFrom).addCase(MessageCreateEvent.class,
                                                                                                                      type -> finalParam[ref.i] = event)
                .addCase(Discord.class, type -> finalParam[ref.i] = discord)
                .addCase(Server.class, type -> finalParam[ref.i] = server)
                .addCase(TextChannel.class, type -> finalParam[ref.i] = channel)
                .addCase(Message.class, type -> finalParam[ref.i] = message)
                .addCase(Author.class, type -> finalParam[ref.i] = author)
                .addCase(String.class, type -> finalParam[ref.i] = content)
                .defaultCase(type -> finalParam[ref.i] = null);
        for (ref.i = 0; ref.i < parameterTypes.length; ref.i++) {
            classSwitch.test(parameterTypes[ref.i]);
        }
        
        discord.getThreadPool()
                .execute(() -> {
                    try {
                        method.invoke(this, finalParam);
                    } catch (IllegalAccessException | InvocationTargetException e) {
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
            List<String> split = List.of(messageContent.split(" "));
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
    
    /**
     * Tries to register all {@link Command} annotated methods of the given class as commands.
     *
     * @param commandClass The class to register command methods in.
     * @throws IllegalArgumentException  If a duplicate alias was found.
     * @throws InvalidParameterException If a private-only marked method has {@link Server} as a parameter.
     * @throws IllegalStateException     If a Command-Method is not {@link Modifier#STATIC}.
     */
    public void registerCommands(Class commandClass) throws IllegalArgumentException, IllegalStateException {
        Stream.of(commandClass.getMethods())
                .filter(method -> method.isAnnotationPresent(Command.class))
                .forEach(this::registerCommandMethod);
    }
    
    /**
     * Registers a {@link Command} annotated method as a command.
     *
     * @param commandMethod The method to register.
     * @throws IllegalArgumentException  If a duplicate alias was found.
     * @throws InvalidParameterException If a private-only marked method has {@link Server} as a parameter.
     * @throws IllegalStateException     The Command-Method is not {@link Modifier#STATIC}.
     */
    public void registerCommands(Method commandMethod) throws IllegalArgumentException, IllegalStateException {
        registerCommandMethod(commandMethod);
    }
    
    /**
     * Tries to unregister a {@link Command} annotated method.
     *
     * @param commandMethod The method to unregister.
     * @return Whether the command could be unregistered.
     */
    public boolean unregisterCommand(Method commandMethod) {
        return commands.stream()
                .filter(instance -> instance.method == commandMethod)
                .findAny()
                .map(commands::remove)
                .orElse(false);
    }
    
    private void registerCommandMethod(Method method) throws IllegalArgumentException, IllegalStateException {
        if (!method.isAnnotationPresent(Command.class)) throw new IllegalArgumentException(
                "Method " + method.toGenericString() + " does not have annotation " + Command.class + ".");
        Command commandAnnot = method.getAnnotationsByType(Command.class)[0];
        if (!Modifier.isStatic(method.getModifiers())) throw new IllegalStateException("The command methods must be static.");
        if (!commandAnnot.enableServerChat() && Set.of(method.getParameterTypes())
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
                .anyMatch(alias -> List.of(aliases)
                        .contains(alias));
    }
    
    private class Instance {
        private final Method  method;
        private final Command annotation;
        
        private Instance(Method method, Command annotation) {
            this.method = method;
            this.annotation = annotation;
        }
    }
    
    // Static members
    
    /**
     * This method describes the default help command. This command is being enabled by passing {@code TRUE} as third
     * argument when initializing the Command Framework.
     *
     * @param discord A discord.
     * @param server  A server.
     * @param author  An author.
     * @param channel A channel.
     */
    @Command(aliases = "help", description = "Shows a list of all commands.")
    public static void defaultHelp(Discord discord, Server server, Author author, TextChannel channel) {
        CommandFramework framework = discord.getUtilities()
                .getCommandFramework();
        PagedEmbed embed = new PagedEmbed(channel, () -> {
            Embed.Builder builder = Embed.BUILDER();
            Self self = discord.getSelf();
            
            self.getAvatarUrl()
                    .map(URL::toExternalForm)
                    .ifPresent(builder::setThumbnail);
            builder.setTitle(self.getDisplayName(server) + "'s Commands");
            author.toAuthorUser()
                    .ifPresent(user -> user.getAvatarUrl()
                            .map(URL::toExternalForm)
                            .ifPresentOrElse(ava -> builder.setFooter("Requested by " + user.getDisplayName(server), ava),
                                             () -> builder.setFooter("Requested by " + user.getDisplayName(server))));
            
            return builder;
        });
        framework.commands.stream()
                .map(instance -> instance.annotation)
                .forEach(annotation -> {
                    if (annotation.shownInDefaultHelp()) {
                        StringBuilder sb = new StringBuilder();
                        String description = annotation.description();
                        
                        sb.append(description)
                                .append("\n\n")
                                .append(annotation.enableServerChat() ? "✅" : "❌")
                                .append(" Server Chat | ")
                                .append(annotation.enablePrivateChat() ? "✅" : "❌")
                                .append(" Private Chat")
                                .append("\n")
                                .append("Required Discord Permission: ")
                                .append(annotation.requiredDiscordPermission()
                                                .name())
                                .append("\n")
                                .append(annotation.requireChannelMentions() == 0 ? "" : "Required Channel mentions: " + annotation.requireChannelMentions())
                                .append("\n")
                                .append(annotation.requireUserMentions() == 0 ? "" : "Required User mentions: " + annotation.requireUserMentions())
                                .append("\n")
                                .append(annotation.requireRoleMentions() == 0 ? "" : "Required Role mentions: " + annotation.requireRoleMentions());
                        
                        StringBuilder aliases = new StringBuilder(framework.prefix);
                        Iterator<String> iterator = List.of(annotation.aliases())
                                .iterator();
                        while (iterator.hasNext()) {
                            String next = iterator.next();
                            aliases.append(next);
                            if (iterator.hasNext()) aliases.append(" | ");
                        }
                        
                        embed.addField(aliases.toString(), sb.toString());
                    }
                });
        embed.build();
    }
}
