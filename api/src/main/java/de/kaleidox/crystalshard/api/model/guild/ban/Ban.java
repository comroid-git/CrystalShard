package de.kaleidox.crystalshard.api.model.guild.ban;

import java.util.Optional;

import de.kaleidox.crystalshard.api.entity.user.User;

public interface Ban {
    Optional<String> getReason();

    User getBannedUser();
}
