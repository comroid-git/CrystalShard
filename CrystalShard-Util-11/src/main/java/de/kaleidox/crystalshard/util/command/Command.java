package de.kaleidox.crystalshard.util.command;

import de.kaleidox.crystalshard.core.concurrent.ThreadPool;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.handling.event.message.generic.MessageCreateEvent;
import de.kaleidox.crystalshard.main.items.channel.TextChannel;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.items.permission.Permission;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.user.Author;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation marks a command method, used by the {@link CommandFramework} to determine a command method.
 * <p>
 * Commands are always executed in {@link ThreadPool.Worker} threads.
 * <p>
 * Command methods may accept a varying assortment of parameters in any order. Supported Parameter types are: - {@link
 * MessageCreateEvent} - The event that triggered the handler. - {@link Discord} - The event's discord object. - {@link
 * Server} - The server that the command has been sent in. Will be {@code null} on private messages. - {@link
 * TextChannel} - The channel that the command message has been sent in. - {@link Message} - The command message of the
 * event. - {@link Author} - The author of the command message. - {@link String} - The contents of the command message.
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
     * Default value is {@link Permission#EMPTY}.
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
}
