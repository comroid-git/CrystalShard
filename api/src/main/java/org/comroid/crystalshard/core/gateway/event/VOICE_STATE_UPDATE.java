package org.comroid.crystalshard.core.gateway.event;

// https://discordapp.com/developers/docs/topics/gateway#voice-state-update

import org.comroid.crystalshard.adapter.MainAPI;
import org.comroid.crystalshard.api.model.voice.VoiceState;
import org.comroid.crystalshard.util.model.serialization.JSONBinding;
import org.comroid.crystalshard.util.model.serialization.JSONBindingLocation;

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
