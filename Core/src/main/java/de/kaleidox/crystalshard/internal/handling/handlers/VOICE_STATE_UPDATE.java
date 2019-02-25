package de.kaleidox.crystalshard.internal.handling.handlers;

import com.fasterxml.jackson.databind.JsonNode;

import de.kaleidox.crystalshard.api.entity.server.Server;
import de.kaleidox.crystalshard.api.entity.server.VoiceState;
import de.kaleidox.crystalshard.api.entity.user.User;
import de.kaleidox.crystalshard.api.handling.editevent.EditTrait;
import de.kaleidox.crystalshard.api.handling.listener.voice.VoiceStateUpdateListener;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.voice.VoiceStateUpdateEventInternal;
import de.kaleidox.crystalshard.internal.items.server.VoiceStateInternal;

import java.util.Set;

public class VOICE_STATE_UPDATE extends HandlerBase {
    // Override Methods
    @Override
    public void handle(DiscordInternal discord, JsonNode data) {
        VoiceStateInternal state = (VoiceStateInternal) VoiceStateInternal.getInstance(discord, data);
        Server server = state.getServer()
                .orElse(null);
        User user = state.getUser();

        Set<EditTrait<VoiceState>> traits = state.updateData(data);
        VoiceStateUpdateEventInternal event = new VoiceStateUpdateEventInternal(discord, state, traits);

        collectListeners(VoiceStateUpdateListener.class, discord, server, user).forEach(listener -> discord.getThreadPool()
                .execute(() -> listener.onVoiceStateUpdate(event)));
    }
}
