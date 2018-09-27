package de.kaleidox.crystalshard.internal.handling.event.voice;

import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.EventBase;
import de.kaleidox.crystalshard.main.handling.editevent.EditTrait;
import de.kaleidox.crystalshard.main.handling.event.voice.VoiceStateUpdateEvent;
import de.kaleidox.crystalshard.main.items.server.VoiceState;
import java.util.Set;

public class VoiceStateUpdateEventInternal extends EventBase implements VoiceStateUpdateEvent {
    private final VoiceState                 voiceState;
    private final Set<EditTrait<VoiceState>> traits;
    
    public VoiceStateUpdateEventInternal(DiscordInternal discordInternal, VoiceState voiceState, Set<EditTrait<VoiceState>> traits) {
        super(discordInternal);
        this.voiceState = voiceState;
        this.traits = traits;
    }
    
    // Override Methods
    @Override
    public Set<EditTrait<VoiceState>> getEditTraits() {
        return traits;
    }
    
    @Override
    public VoiceState getVoiceState() {
        return voiceState;
    }
}
