package org.comroid.crystalshard.cdn;

import org.comroid.crystalshard.DiscordAPI;
import org.comroid.restless.endpoint.AccessibleEndpoint;
import org.intellij.lang.annotations.Language;

import java.util.regex.Pattern;

import static org.comroid.crystalshard.cdn.ImageType.*;
import static org.comroid.crystalshard.entity.Snowflake.ID_REGEX;

public enum CDNEndpoint implements AccessibleEndpoint {
    CUSTOM_EMOJI("emojis/%s.%s", ID_REGEX, combine(PNG, GIF)),

    GUILD_ICON("icons/%s/%s.%s", ID_REGEX, ".", combine(PNG, JPEG, WebP, GIF)),
    GUILD_SPLASH("splashes/%s/%s.%s", ID_REGEX, ".", combine(PNG, JPEG, WebP)),
    GUILD_DISCOVERY_SPLASH("discovery-splashes/%s/%s.%s", ID_REGEX, ".", combine(PNG, JPEG, WebP)),
    GUILD_BANNER("banners/%s/%s.%s", ID_REGEX, ".", combine(PNG, JPEG, WebP)),

    DEFAULT_USER_AVATAR("embed/avatars/%s.%s", "[012345]", PNG.getRegex()),
    USER_AVATAR("avatars/%s/%s.%s", ID_REGEX, ".", combine(PNG, JPEG, WebP, GIF)),

    APPLICATION_ICON("app-icons/%s/%s.%s", ID_REGEX, ".", combine(PNG, JPEG, WebP)),
    APPLICATION_ASSET("app-assets/%s/%s.%s", ID_REGEX, ".", combine(PNG, JPEG, WebP)),

    ACHIEVEMENT_ICON("app-assets/%s/achievements/%s/icons/%s.%s", ID_REGEX, ".", ".", combine(PNG, JPEG, WebP)),

    TEAM_ICON("team-icons/%s/%s.%s", ".", ".", combine(PNG, JPEG, WebP, GIF));

    private final String extension;
    private final String[] regex;
    private final Pattern pattern;

    @Override
    public String getUrlBase() {
        return DiscordAPI.CDN_URL_BASE;
    }

    @Override
    public String getUrlExtension() {
        return extension;
    }

    @Override
    public String[] getRegExpGroups() {
        return regex;
    }

    @Override
    public Pattern getPattern() {
        return pattern;
    }

    CDNEndpoint(String extension, @Language("RegExp") String... regex) {
        this.extension = extension;
        this.regex = regex;
        this.pattern = buildUrlPattern();
    }
}
