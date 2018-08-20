package de.kaleidox.crystalshard.internal.items.user;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.user.User;
import de.kaleidox.logging.Logger;
import de.kaleidox.util.UrlHelper;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

@SuppressWarnings("unused")
public class UserInternal implements User {
    private final static Logger logger = new Logger(User.class);
    private final long id;
    private final String name;
    private final String discriminator;
    private final URL avatarUrl;
    private final boolean bot;
    private final boolean mfa;
    private final boolean verified;
    private final String locale;
    private final String email;
    private final Discord discord;

    public UserInternal(Discord discord, JsonNode data) {
        this.discord = discord;
        this.id = data.get("id").asLong();
        this.name = data.get("name").asText();
        this.discriminator = data.get("discriminator").asText();
        this.avatarUrl = data.has("avatar_url") ?
                UrlHelper.orNull(data.get("avatar_url").asText()) : null;
        this.bot = data.get("bot").asBoolean(false);
        //noinspection SimplifiableConditionalExpression
        this.mfa = data.has("mfa") ?
                data.get("mfa_enabled").asBoolean(false) : false;
        this.locale = data.has("locale") ?
                data.get("locale").asText(null) : null;
        //noinspection SimplifiableConditionalExpression
        this.verified = data.has("verified") ?
                data.get("verified").asBoolean(false) : false;
        this.email = data.has("email") ?
                data.get("email").asText(null) : null;
    }

    @Override
    public String getDiscriminatedName() {
        return name + "#" + discriminator;
    }

    @Override
    public String getDiscriminator() {
        return discriminator;
    }

    @Override
    public Optional<String> getNickname(Server inServer) {
        // todo This
        return null;
    }

    @Override
    public String getDisplayName(Server inServer) {
        return getNickname(inServer).orElseGet(this::getName);
    }

    @Override
    public String getNicknameMentionTag() {
        return "<@!" + id + ">";
    }

    @Override
    public Optional<URL> getAvatarUrl() {
        return Optional.ofNullable(avatarUrl);
    }

    @Override
    public boolean isBot() {
        return bot;
    }

    @Override
    public boolean isVerified() {
        return verified;
    }

    @Override
    public boolean hasMultiFactorAuthorization() {
        return mfa;
    }

    @Override
    public Optional<String> getLocale() {
        return Optional.ofNullable(locale);
    }

    @Override
    public Optional<String> getEmail() {
        return Optional.ofNullable(email);
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public Discord getDiscord() {
        return discord;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getMentionTag() {
        return "<@" + id + ">";
    }
}
