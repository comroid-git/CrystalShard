package de.kaleidox.crystalshard.api.handling.editevent.enums;

import de.kaleidox.crystalshard.api.entity.user.User;
import de.kaleidox.crystalshard.api.handling.editevent.EditTrait;

public enum UserEditTrait implements EditTrait<User> {
    USERNAME,
    DISCRIMINATOR,
    AVATAR,
    MFA_STATE,
    LOCALE,
    VERIFIED_STATE, // requires OAuth Scope: email

    EMAIL // requires OAuth2 Scope: email
}
