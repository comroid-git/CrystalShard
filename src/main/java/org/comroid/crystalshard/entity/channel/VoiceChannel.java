package org.comroid.crystalshard.entity.channel;

import org.comroid.util.StandardValueType;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;

public interface VoiceChannel extends Channel {
    GroupBind<VoiceChannel> BASETYPE
            = Channel.BASETYPE.subGroup("voice-channel");
    VarBind<VoiceChannel, Integer, Integer, Integer> BITRATE
            = BASETYPE.createBind("bitrate")
            .extractAs(StandardValueType.INTEGER)
            .build();
    VarBind<VoiceChannel, Integer, Integer, Integer> USER_LIMIT
            = BASETYPE.createBind("user_limit")
            .extractAs(StandardValueType.INTEGER)
            .build();
}
