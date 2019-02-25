package de.kaleidox.crystalshard.api.util;

import java.util.regex.Pattern;

public class DiscordMentiontagPatterns {
    // Static Fields
    public static final Pattern USER_MENTION = Pattern.compile("(?<!(?<!\\\\)(?:\\\\{2}+){0,1000000000}+\\\\)<@!?+(?<id>[0-9]++)>");
    public static final Pattern ROLE_MENTION = Pattern.compile("(?<!(?<!\\\\)(?:\\\\{2}+){0,1000000000}+\\\\)<@&(?<id>[0-9]++)>");
    public static final Pattern CHANNEL_MENTION = Pattern.compile("(?<!(?<!\\\\)(?:\\\\{2}+){0,1000000000}+\\\\)(?-x:<#)(?<id>[0-9]++)>");
    public static final Pattern CUSTOM_EMOJI = Pattern.compile("(?<!(?<!\\\\)(?:\\\\{2}+){0,1000000000}+\\\\)<a?+:(?<name>\\w++):(?<id>[0-9]++)>");
}
