package de.kaleidox.crystalshard.main.event;

import de.kaleidox.crystalshard.main.items.user.User;

import java.util.concurrent.CompletableFuture;

public interface UserEvent {
    User getUser();

    CompletableFuture<User> requestUser();
}
