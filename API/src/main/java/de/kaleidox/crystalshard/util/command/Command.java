package de.kaleidox.crystalshard.util.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import java.util.Optional;

import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.entity.channel.Channel;
import de.kaleidox.crystalshard.api.entity.channel.PrivateTextChannel;
import de.kaleidox.crystalshard.api.entity.channel.ServerTextChannel;
import de.kaleidox.crystalshard.api.entity.channel.TextChannel;
import de.kaleidox.crystalshard.api.entity.message.Message;
import de.kaleidox.crystalshard.api.entity.server.permission.Permission;
import de.kaleidox.crystalshard.api.entity.role.Role;
import de.kaleidox.crystalshard.api.entity.server.Server;
import de.kaleidox.crystalshard.api.entity.user.Author;
import de.kaleidox.crystalshard.api.entity.user.User;
import de.kaleidox.crystalshard.api.handling.event.message.generic.MessageCreateEvent;
import de.kaleidox.crystalshard.core.concurrent.ThreadPool;

/**
 * This annotation marks a command method, used by the {@link CommandFramework} to determine a command method.
 * <p>
 * Commands are always executed in {@link ThreadPool} threads.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Command {
    /**
     * Defines aliases for the command. Must not contain {@code null}.
     *
     * @return The aliases of the command.
     */
    String[] aliases();

    /**
     * Defines the description that is shown in the default help command.
     *
     * @return The description of the command.
     */
    String description() default "No description provided.";

    /**
     * Defines whether the command is shown in the default help command. Default value is {@code TRUE}.
     *
     * @return Whether to list this command in the default help command.
     */
    boolean shownInDefaultHelp() default true;

    /**
     * Defines whether this command should be usable in a private chat. Default value is {@code true}.
     *
     * @return Whether this command is available in private chat.
     */
    boolean enablePrivateChat() default true;

    /**
     * Defines whether this command should be usable in a server chat. Default value is {@code true}.
     *
     * @return Whether this command is available in server chat.
     */
    boolean enableServerChat() default true;

    /**
     * Defines a permission that the user is required to have be in the executing context; e.g. the ServerTextChannel.
     * Default value is {@link
     * Permission#EMPTY}.
     *
     * @return The required permission to execute this command.
     */
    Permission requiredDiscordPermission() default Permission.EMPTY;

    /**
     * Defines the minimum amount of channel mentions required for the command to run. Default value is {@code 0}.
     *
     * @return The required minimum amount of channel mentions.
     */
    int requireChannelMentions() default 0;

    /**
     * Defines the minimum amount of user mentions required for the command to run. Default value is {@code 0}.
     *
     * @return The required minimum amount of user mentions.
     */
    int requireUserMentions() default 0;

    /**
     * Defines the minimum amount of role mentions required for the command to run. Default value is {@code 0}.
     *
     * @return The required minimum amount of role mentions.
     */
    int requireRoleMentions() default 0;

    /**
     * Defines whether the Command should respond with the default {@link Command.ExecutionState} object.
     *
     * @return Whether to use the default {@link Command.ExecutionState}.
     */
    boolean useExecutionState() default false;

    /**
     * Command parameter structure
     */
    interface Parameters {
        /**
         * Returns the active Discord object.
         *
         * @return The active Discord object.
         */
        Discord getDiscord();

        /**
         * The event triggered by the command execution.
         *
         * @return The event that triggered the command exection.
         */
        MessageCreateEvent getEvent();

        /**
         * The Server the command was sent in.
         *
         * @return An optional server that the command was sent in.
         */
        Optional<Server> getServer();

        /**
         * Privacy state of the command.
         *
         * @return Whether the command was sent in a private context.
         */
        default boolean isPrivate() {
            return getServer().isEmpty();
        }

        TextChannel getTextChannel();

        default Optional<ServerTextChannel> getServerTextChannel() {
            return getTextChannel().asServerTextChannel();
        }

        default Optional<PrivateTextChannel> getPrivateTextChannel() {
            return getTextChannel().asPrivateTextChannel();
        }

        Message getCommandMessage();

        default List<Channel> getChannelMentions() {
            return getCommandMessage().getChannelMentions();
        }

        default List<User> getUserMentions() {
            return getCommandMessage().getUserMentions();
        }

        default List<Role> getRoleMentions() {
            return getCommandMessage().getRoleMentions();
        }

        Author getCommandExecutor();

        ExecutionState executionState();
    }

    interface ExecutionState {
        // todo
    }
}
