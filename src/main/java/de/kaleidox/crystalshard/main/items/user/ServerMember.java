package de.kaleidox.crystalshard.main.items.user;

import de.kaleidox.crystalshard.main.UserContainer;

public interface ServerMember extends User {
    static ServerMember of(UserContainer server, long userId) {
        return null; // todo
    }
}
