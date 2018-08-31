package de.kaleidox.crystalshard.main.items.user;

import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.user.presence.PresenceState;
import de.kaleidox.crystalshard.main.items.user.presence.UserActivity;

import java.net.URL;
import java.util.concurrent.CompletableFuture;

public interface Self extends User {
    CompletableFuture<Void> setName(String name);

    CompletableFuture<Void> setNickname(String nickname, Server inServer);

    CompletableFuture<Void> setAvatar(URL avatarUrl);

    CompletableFuture<Void> setPresence(UserActivity.ActivityType activityType, String description);

    CompletableFuture<Void> setPresence(UserActivity.ActivityType activityType, String description, String url);
}
