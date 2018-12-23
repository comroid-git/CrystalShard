package de.kaleidox.crystalshard.api.handling.editevent.enums;

import de.kaleidox.crystalshard.api.handling.editevent.EditTrait;
import de.kaleidox.crystalshard.api.entity.server.VoiceState;

public enum VoiceStateEditTrait implements EditTrait<VoiceState> {
    CHANNEL,
    DEAFENED_STATE,
    MUTED_STATE,
    SELF_DEAFENED_STATE,
    SELF_MUTED_STATE,
    SUPPRESSED_STATE
}
