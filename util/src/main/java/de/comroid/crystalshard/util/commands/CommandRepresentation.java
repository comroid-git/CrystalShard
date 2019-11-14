package de.comroid.crystalshard.util.commands;

import java.lang.reflect.Method;

import org.javacord.api.entity.permission.PermissionType;
import org.jetbrains.annotations.Nullable;

public final class CommandRepresentation {
    public final Method method;
    public final String[] aliases;
    public final String description;
    public final String usage;
    public final int ordinal;
    public final boolean showInHelpCommand;
    public final boolean enablePrivateChat;
    public final boolean enableServerChat;
    public final PermissionType[] requiredDiscordPermissions;
    public final int minimumArguments;
    public final int maximumArguments;
    public final int requiredChannelMentions;
    public final int requiredUserMentions;
    public final int requiredRoleMentions;
    public final boolean runInNSFWChannelOnly;
    public final boolean convertStringResultsToEmbed;
    public final boolean useTypingIndicator;
    public final boolean async;
    public @Nullable final String groupName;
    public @Nullable final String groupDescription;
    public final int groupOrdinal;
    public @Nullable final Object invocationTarget;

    CommandRepresentation(
            Method method,
            Command cmd,
            @Nullable CommandGroup group,
            @Nullable Object invocationTarget
    ) {
        this.method = method;
        this.invocationTarget = invocationTarget;

        this.aliases = (cmd.aliases().length == 0 ? new String[]{method.getName()} : cmd.aliases());
        this.description = cmd.description();
        this.usage = cmd.usage();
        this.ordinal = cmd.ordinal();
        this.showInHelpCommand = cmd.shownInHelpCommand();
        this.enablePrivateChat = cmd.enablePrivateChat();
        this.enableServerChat = cmd.enableServerChat();
        this.requiredDiscordPermissions = cmd.requiredDiscordPermissions();
        this.minimumArguments = cmd.minimumArguments();
        this.maximumArguments = cmd.maximumArguments();
        this.requiredChannelMentions = cmd.requiredChannelMentions();
        this.requiredUserMentions = cmd.requiredUserMentions();
        this.requiredRoleMentions = cmd.requiredRoleMentions();
        this.runInNSFWChannelOnly = cmd.runInNSFWChannelOnly();
        this.convertStringResultsToEmbed = cmd.convertStringResultsToEmbed();
        this.useTypingIndicator = cmd.useTypingIndicator();
        this.async = cmd.async();

        if (group != null) {
            if (!group.name().equals(CommandHandler.NO_GROUP))
                groupName = group.name();
            else
                groupName = method.getDeclaringClass().getSimpleName();

            if (!group.description().equals(CommandHandler.NO_GROUP))
                groupDescription = group.description();
            else
                groupDescription = "No group description provided.";

            groupOrdinal = group.ordinal();
        } else {
            groupName = null;
            groupDescription = null;
            groupOrdinal = Integer.MAX_VALUE;
        }
    }

    CommandRepresentation(
            Method method,
            Command cmd,
            @SuppressWarnings("NullableProblems") String groupName,
            @Nullable String groupDescription,
            int groupOrdinal,
            @Nullable Object invocationTarget
    ) {
        this.method = method;
        this.invocationTarget = invocationTarget;

        this.aliases = (cmd.aliases().length == 0 ? new String[]{method.getName()} : cmd.aliases());
        this.description = cmd.description();
        this.usage = cmd.usage();
        this.ordinal = cmd.ordinal();
        this.showInHelpCommand = cmd.shownInHelpCommand();
        this.enablePrivateChat = cmd.enablePrivateChat();
        this.enableServerChat = cmd.enableServerChat();
        this.requiredDiscordPermissions = cmd.requiredDiscordPermissions();
        this.minimumArguments = cmd.minimumArguments();
        this.maximumArguments = cmd.maximumArguments();
        this.requiredChannelMentions = cmd.requiredChannelMentions();
        this.requiredUserMentions = cmd.requiredUserMentions();
        this.requiredRoleMentions = cmd.requiredRoleMentions();
        this.runInNSFWChannelOnly = cmd.runInNSFWChannelOnly();
        this.convertStringResultsToEmbed = cmd.convertStringResultsToEmbed();
        this.useTypingIndicator = cmd.useTypingIndicator();
        this.async = cmd.async();

        this.groupName = groupName;
        this.groupDescription = groupDescription;
        this.groupOrdinal = groupOrdinal;
    }

    @Override
    public String toString() {
        return method.getName();
    }
}
