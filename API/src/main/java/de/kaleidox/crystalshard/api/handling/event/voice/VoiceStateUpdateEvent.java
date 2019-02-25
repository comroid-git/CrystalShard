package de.kaleidox.crystalshard.api.handling.event.voice;

import de.kaleidox.crystalshard.api.entity.server.VoiceState;
import de.kaleidox.crystalshard.api.handling.editevent.EditEvent;

public interface VoiceStateUpdateEvent extends VoiceEvent, EditEvent<VoiceState> {
    VoiceState getVoiceState();
}
