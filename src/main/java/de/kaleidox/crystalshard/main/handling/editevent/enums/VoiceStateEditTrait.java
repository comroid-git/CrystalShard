package de.kaleidox.crystalshard.main.handling.editevent.enums;

import de.kaleidox.crystalshard.main.handling.editevent.EditTrait;
import de.kaleidox.crystalshard.main.items.server.VoiceState;

public enum VoiceStateEditTrait implements EditTrait<VoiceState> {
    CHANNEL,
    DEAFENED_STATE,
    MUTED_STATE,
    SELF_DEAFENED_STATE,
    SELF_MUTED_STATE,
    SUPPRESSED_STATE
}
