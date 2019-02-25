package de.kaleidox.crystalshard.internal.handling.event.user;

import de.kaleidox.crystalshard.api.entity.user.User;
import de.kaleidox.crystalshard.api.handling.editevent.EditTrait;
import de.kaleidox.crystalshard.api.handling.event.user.UserUpdateEvent;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.EventBase;

import java.util.Set;

public class UserUpdateEventInternal extends EventBase implements UserUpdateEvent {
    private final User user;
    private final Set<EditTrait<User>> traits;

    public UserUpdateEventInternal(DiscordInternal discordInternal, User user, Set<EditTrait<User>> traits) {
        super(discordInternal);
        this.user = user;
        this.traits = traits;
    }

    // Override Methods
    @Override
    public Set<EditTrait<User>> getEditTraits() {
        return traits;
    }

    @Override
    public User getUser() {
        return user;
    }
}
