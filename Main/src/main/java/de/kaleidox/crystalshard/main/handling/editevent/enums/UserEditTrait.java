package de.kaleidox.crystalshard.main.handling.editevent.enums;

import de.kaleidox.crystalshard.main.handling.editevent.EditTrait;
import de.kaleidox.crystalshard.main.items.user.User;

public enum UserEditTrait implements EditTrait<User> {
    USERNAME,
    DISCRIMINATOR,
    AVATAR,
    MFA_STATE,
    LOCALE,
    VERIFIED_STATE, // requires OAuth Scope: email

    EMAIL // requires OAuth2 Scope: email
}
