package de.kaleidox.crystalshard.api.model.user;

import java.net.URL;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import de.kaleidox.crystalshard.api.entity.channel.PrivateTextChannel;
import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.api.entity.user.User;
import de.kaleidox.crystalshard.util.annotation.IntroducedBy;

import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;

public interface Yourself extends User {
    Updater createUpdater();

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/user#get-current-user-guilds")
    CompletableFuture<Collection<Guild>> requestGuilds();

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/user#get-user-dms")
    CompletableFuture<Collection<PrivateTextChannel>> requestPrivateMessageChannels();

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/user#get-user-connections")
    CompletableFuture<Collection<User.Connection>> requestConnections();

    interface Updater {
        String getUsername();

        Updater setUsername(String username);

        URL getAvatarURL();

        Updater setAvatar(URL url);

        @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/user#modify-current-user")
        CompletableFuture<Yourself> update();
    }
}
