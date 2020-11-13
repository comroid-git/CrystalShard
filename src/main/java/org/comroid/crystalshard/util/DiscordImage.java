package org.comroid.crystalshard.util;

import org.comroid.restless.endpoint.AccessibleEndpoint;
import org.intellij.lang.annotations.Language;

import static org.comroid.crystalshard.entity.Snowflake.ID_REGEX;
import static org.comroid.crystalshard.util.ImageType.*;

public enum DiscordImage implements AccessibleEndpoint {
    CUSTOM_EMOJI("emojis/%s.%s",
            ID_REGEX,
            combineRegEx(PNG, GIF)),
    GUILD_ICON("icons/%s/%s.%s",
            ID_REGEX,
            ".+",
            combineRegEx(PNG, JPEG, WebP, GIF)),
    GUILD_SPLASH("splashes/%s/%s.%s",
            ID_REGEX,
            ".+",
            combineRegEx(PNG, JPEG, WebP)),
    GUILD_DISCOVERY_SPLASH("discovery-splashes/%s/%s.%s",
            ID_REGEX,
            ".+",
            combineRegEx(PNG, JPEG, WebP)),
    GUILD_BANNER("banners/%s/%s.%s",
            ID_REGEX,
            ".+",
            combineRegEx(PNG, JPEG, WebP)),
    DEFAULT_USER_AVATAR("embed/avatars/%s.%s",
            "\\d+?",
            ".+",
            combineRegEx(PNG)),
    USER_AVATAR("avatars/%s/%s.png",
            ID_REGEX,
            ".+",
            combineRegEx(PNG, JPEG, WebP, GIF)),
    APPLICATION_ICON("app-icon/%s/%s.%s",
            ID_REGEX,
            ".+",
            combineRegEx(PNG, JPEG, WebP)),
    APPLICATION_ASSET("app-assets/%s/%s",
            ID_REGEX,
            ID_REGEX,
            ".+",
            combineRegEx(PNG, JPEG, WebP)),
    ACHIEVEMENT_ICON("app-assets/%s/achievements/%s/icons/%s.%s",
            ID_REGEX,
            ID_REGEX,
            ".+",
            combineRegEx(PNG, JPEG, WebP)),
    TEAM_ICON("team-icons/%s/%s.%s",
            ID_REGEX,
            ".+",
            combineRegEx(PNG, JPEG, WebP));


    private static final String URL_BASE = "https://cdn.discordapp.com/";
    private final String extension;
    private final String[] regexp;

    @Override
    public String getUrlBase() {
        return URL_BASE;
    }

    @Override
    public String getUrlExtension() {
        return extension;
    }

    @Override
    public String[] getRegExpGroups() {
        return regexp;
    }

    DiscordImage(String extension, @Language("RegExp") String... regexp) {
        this.extension = extension;
        this.regexp = regexp;
    }
}
