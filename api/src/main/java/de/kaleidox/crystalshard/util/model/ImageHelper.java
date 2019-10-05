package de.kaleidox.crystalshard.util.model;

import java.net.MalformedURLException;
import java.net.URL;

import de.kaleidox.crystalshard.util.annotation.IntroducedBy;

import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;
import static de.kaleidox.crystalshard.util.model.FileType.GIF;
import static de.kaleidox.crystalshard.util.model.FileType.JPEG;
import static de.kaleidox.crystalshard.util.model.FileType.PNG;
import static de.kaleidox.crystalshard.util.model.FileType.WebP;

@IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/reference#image-formatting")
public enum ImageHelper {
    CUSTOM_EMOJI("emojis/%s.%s", PNG, GIF),
    GUILD_ICON("icons/%s/%s.%s", PNG, JPEG, WebP, GIF),
    GUILD_SPLASH("splashes/%s/%s.%s", PNG, JPEG, WebP),
    GUILD_BANNER("banners/%s/%s.%s", PNG, JPEG, WebP),
    DEFAULT_USER_AVATAR("embed/avatars/%s.%s", PNG) {
        @Override
        public URL url(FileType type, Object... args) throws IllegalArgumentException {
            args[0] = Integer.parseInt(args[0].toString()) % 5;
            return super.url(type, args);
        }
    },
    USER_AVATAR("avatars/%s/%s.%s", PNG, JPEG, WebP, GIF),
    APPLICATION_ICON("app-icons/%s/%s.%s", PNG, JPEG, WebP),
    APPLICATION_ASSET("app-assets/%s/%s.%s", PNG, JPEG, WebP),
    ACHIEVEMENT_ICON("app-assets/%s/achievements/%s/icons/%s.%s", PNG, JPEG, WebP),
    TEAM_ICON("team-icons/%s/%s.%s", PNG, JPEG, WebP);
    
    public static final String IMAGE_BASE_URL = "https://cdn.discordapp.com/";
    
    private final String unformattedPath;
    private final FileType[] supportedTypes;

    ImageHelper(String appendix, FileType... supportedTypes) {
        this.unformattedPath = IMAGE_BASE_URL + appendix;
        this.supportedTypes = supportedTypes;
    }
    
    public URL url(FileType type, Object... args) throws IllegalArgumentException {
        final int argumentCount = getArgumentCount();
        
        if (args.length != argumentCount)
            throw new IllegalArgumentException(String.format("Illegal argument count: expected: %d  actual: %d", argumentCount, args.length));
        
        Object[] formats = new String[args.length + 1];
        for (int i = 0; i < args.length; i++) formats[i] = args[i].toString();
        formats[formats.length + 1] = type.name().toLowerCase(); 

        try {
            return new URL(String.format(unformattedPath, formats));
        } catch (MalformedURLException e) {
            throw new AssertionError(e);
        }
    }
    
    public int getArgumentCount() {
        return unformattedPath.split("%s").length - 2; // -2 because the type isnt included
    }
}
