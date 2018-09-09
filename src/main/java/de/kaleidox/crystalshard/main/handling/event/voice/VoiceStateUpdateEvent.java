package de.kaleidox.crystalshard.main.handling.event.voice;

import de.kaleidox.crystalshard.main.handling.editevent.EditEvent;
import de.kaleidox.crystalshard.main.items.server.VoiceState;

public interface VoiceStateUpdateEvent extends VoiceEvent, EditEvent<VoiceState> {
}
