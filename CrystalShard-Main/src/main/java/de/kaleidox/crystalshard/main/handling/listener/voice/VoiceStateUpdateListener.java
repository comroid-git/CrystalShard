package de.kaleidox.crystalshard.main.handling.listener.voice;

import de.kaleidox.crystalshard.main.handling.event.voice.VoiceStateUpdateEvent;
import de.kaleidox.crystalshard.main.handling.listener.DiscordAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.server.ServerAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.user.UserAttachableListener;

@FunctionalInterface
public interface VoiceStateUpdateListener extends DiscordAttachableListener, ServerAttachableListener, UserAttachableListener {
    void onVoiceStateUpdate(VoiceStateUpdateEvent event);
}
