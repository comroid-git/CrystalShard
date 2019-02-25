package de.kaleidox.crystalshard.api.entity.user;

import org.intellij.lang.annotations.MagicConstant;

import de.kaleidox.crystalshard.api.entity.server.Server;
import de.kaleidox.crystalshard.api.entity.user.presence.Presence;
import de.kaleidox.crystalshard.api.entity.user.presence.UserActivity;

import java.net.URL;
import java.util.concurrent.CompletableFuture;

public interface Self extends User {
    CompletableFuture<Void> setName(String name);

    CompletableFuture<Void> setNickname(String nickname, Server inServer);

    CompletableFuture<Void> setAvatar(URL avatarUrl);

    CompletableFuture<Void> setStatus(@MagicConstant(flagsFromClass = Presence.Status.class) String status);

    CompletableFuture<Void> setActivity(UserActivity.Type type, String title);

    CompletableFuture<Void> setActivity(UserActivity.Type type, String title, String url);
}
