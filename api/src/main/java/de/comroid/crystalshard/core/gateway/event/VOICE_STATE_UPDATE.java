package de.comroid.crystalshard.core.gateway.event;

// https://discordapp.com/developers/docs/topics/gateway#voice-state-update

import de.comroid.crystalshard.adapter.MainAPI;
import de.comroid.crystalshard.api.model.voice.VoiceState;
import de.comroid.crystalshard.util.model.serialization.JSONBinding;
import de.comroid.crystalshard.util.model.serialization.JSONBindingLocation;

import com.alibaba.fastjson.JSONObject;

@MainAPI
@JSONBindingLocation(VOICE_STATE_UPDATE.JSON.class)
public interface VOICE_STATE_UPDATE extends GatewayEventBase {
    default VoiceState getVoiceState() {
        return getBindingValue(JSON.VOICE_STATE);
    }
    
    interface JSON {
        JSONBinding.TwoStage<JSONObject, VoiceState> VOICE_STATE = JSONBinding.rooted(VoiceState.class);
    }
}
