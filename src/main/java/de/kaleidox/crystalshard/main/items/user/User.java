package de.kaleidox.crystalshard.main.items.user;

import de.kaleidox.crystalshard.main.items.DiscordItem;
import de.kaleidox.crystalshard.main.items.Mentionable;
import de.kaleidox.crystalshard.main.items.Nameable;
import de.kaleidox.crystalshard.main.items.message.MessageReciever;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.util.Castable;

import java.net.URL;
import java.util.Optional;

public interface User extends DiscordItem, Nameable, Mentionable, MessageReciever, Castable<User> {
    String getDiscriminatedName();

    String getDiscriminator();

    Optional<String> getNickname(Server inServer);

    String getDisplayName(Server inServer);

    String getNicknameMentionTag();

    Optional<URL> getAvatarUrl();

    boolean isBot();

    boolean isVerified();

    boolean hasMultiFactorAuthorization();

    Optional<String> getLocale();

    Optional<String> getEmail();

    default Optional<ServerMember> toServerMember() {
        return castTo(ServerMember.class);
    }
}
