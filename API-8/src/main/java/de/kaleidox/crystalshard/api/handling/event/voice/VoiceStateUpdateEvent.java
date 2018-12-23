package de.kaleidox.crystalshard.api.handling.event.voice;

import de.kaleidox.crystalshard.api.handling.editevent.EditEvent;
import de.kaleidox.crystalshard.api.entity.server.VoiceState;

public interface VoiceStateUpdateEvent extends VoiceEvent, EditEvent<VoiceState> {
    VoiceState getVoiceState();
}
