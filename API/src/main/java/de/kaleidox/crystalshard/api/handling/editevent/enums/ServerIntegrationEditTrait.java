package de.kaleidox.crystalshard.api.handling.editevent.enums;

import de.kaleidox.crystalshard.api.handling.editevent.EditTrait;

public enum ServerIntegrationEditTrait implements EditTrait<Object> {
    CREATION,
    EXPIRE_BEHAVIOUR,
    EXPIRE_GRACE_PERIOD,
    EXPIRE_EMOTICONS,
    DELETION
}
