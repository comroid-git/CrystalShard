package de.kaleidox.crystalshard.main.items.user;

import de.kaleidox.crystalshard.main.util.UserContainer;

public interface ServerMember extends User {
    static ServerMember of(UserContainer container, long userId) {
        return null; // todo
    }
}
