package de.kaleidox.crystalshard.api.handling.listener.voice;

import de.kaleidox.crystalshard.api.handling.event.voice.VoiceStateUpdateEvent;
import de.kaleidox.crystalshard.api.handling.listener.DiscordAttachableListener;
import de.kaleidox.crystalshard.api.handling.listener.server.ServerAttachableListener;
import de.kaleidox.crystalshard.api.handling.listener.user.UserAttachableListener;

@FunctionalInterface
public interface VoiceStateUpdateListener extends DiscordAttachableListener, ServerAttachableListener, UserAttachableListener {
    void onVoiceStateUpdate(VoiceStateUpdateEvent event);
}
