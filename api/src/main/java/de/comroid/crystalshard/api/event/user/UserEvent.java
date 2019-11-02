package de.comroid.crystalshard.api.event.user;

import de.comroid.crystalshard.api.entity.user.User;
import de.comroid.crystalshard.api.event.model.Event;

import org.jetbrains.annotations.NotNull;

public interface UserEvent extends Event {
    User getTriggeringUser();
}
