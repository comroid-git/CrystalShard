package org.comroid.crystalshard.model.guild;

import org.comroid.api.Named;
import org.comroid.common.info.Described;

public enum GuildFeature implements Named, Described {
    INVITE_SPLASH("guild has access to set an invite splash background"),
    VIP_REGIONS("guild has access to set 384kbps bitrate in voice (previously VIP voice servers)"),
    VANITY_URL("guild has access to set a vanity URL"),
    VERIFIED("guild is verified"),
    PARTNERED("guild is partnered"),
    COMMUNITY("guild can enable welcome screen and discovery, and receives community updates"),
    COMMERCE("guild has access to use commerce features (i.e. create store channels)"),
    NEWS("guild has access to create news channels"),
    DISCOVERABLE("guild is lurkable and able to be discovered in the directory"),
    FEATURABLE("guild is able to be featured in the directory"),
    ANIMATED_ICON("guild has access to set an animated guild icon"),
    BANNER("guild has access to set a guild banner image"),
    WELCOME_SCREEN_ENABLED("guild has enabled the welcome screen");

    private final String description;

    @Override
    public String getDescription() {
        return description;
    }

    GuildFeature(String description) {
        this.description = description;
    }
}
