package de.kaleidox.util;

import de.kaleidox.crystalshard.internal.util.Container;
import de.kaleidox.crystalshard.main.items.server.emoji.CustomEmoji;
import de.kaleidox.util.helpers.UrlHelper;

import java.io.File;
import java.net.URL;
import java.util.Objects;

import static de.kaleidox.util.FileType.IMAGE.*;

public class ImageEndpoint {
    public final static String IMAGE_BASE_URL = "https://cdn.discordapp.com/";
    private final URL url;

    private ImageEndpoint(Location location, URL url) {
        this.url = url;
    }

    public URL getUrl() {
        return url;
    }

    public enum Location {
        CUSTOM_EMOJI("emojis/%s.%s", new FileType[]{PNG, GIF}, false),

        GUILD_ICON("icons/%s/%s.%s", IMAGE.NO_GIF, true),

        GUILD_SPLASH("splashes/%s/%s.%s", IMAGE.NO_GIF, true),

        DEFAULT_USER_AVATAR("embed/avatars/%s.%s", new FileType[]{PNG}, false),

        USER_AVATAR("avatars/%s/%s.%s", IMAGE.ALL, true),

        APPLICATION_ICON("app-icons/%s/%s.%s", IMAGE.NO_GIF, true);

        private final String endpoint;
        private final FileType[] supportedTypes;
        private final boolean hasIconHash;

        Location(String endpoint, FileType[] supportedTypes, boolean hasIconHash) {
            this.endpoint = endpoint;
            this.supportedTypes = supportedTypes;
            this.hasIconHash = hasIconHash;
        }

        public int getParameterCount() {
            int splitted = endpoint.split("%s").length - 1;
            int end = (endpoint.indexOf("%s") == endpoint.length() - 2 ? 1 : 0);
            return splitted + end;
        }

        public ImageEndpoint toEndpoint(String iconHash, Object... parameter) {
            String[] params = new String[parameter.length];
            int parameterCount = getParameterCount();

            if ((parameter.length == parameterCount - 1) && iconHash != null) {
                // all is good
            } else if ((parameter.length == parameterCount)) {
                // all is good
            } else throw new IllegalArgumentException("IconHash must not be null if not specified in parameter.");

            for (int i = 0; i < parameter.length; i++) {
                Object x = parameter[i];

                if (x instanceof CustomEmoji) {
                    params[i] = Long.toUnsignedString(((CustomEmoji) x).getId());
                } else if (x instanceof Long) {
                    params[i] = Long.toUnsignedString((Long) x);
                } else {
                    params[i] = x.toString();
                }
            }

            if (hasIconHash && Objects.nonNull(iconHash)) params[params.length - 1] = iconHash;

            if (parameterCount == params.length) {
                String of = String.format(IMAGE_BASE_URL + endpoint, (Object[]) params);
                URL url = UrlHelper.require(of);
                return new ImageEndpoint(this, url);
            } else throw new IllegalArgumentException("Too " + (parameterCount > params.length ? "few" : "many") +
                    " parameters!");
        }

        public Container createForFile(File file) {
            return null; // todo
        }
    }
}
