package org.comroid.crystalshard.api.entity.channel;

import org.comroid.crystalshard.util.annotation.IntroducedBy;
import org.comroid.crystalshard.util.model.serialization.JSONBinding;

import com.alibaba.fastjson.JSONObject;

import static org.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.GETTER;
import static org.comroid.crystalshard.util.model.serialization.JSONBinding.identity;

public interface VoiceChannel extends Channel {
    @IntroducedBy(GETTER)
    default int getBitrate() {
        return wrapBindingValue(Bind.BITRATE).orElse(VoiceChannel.Default.BITRATE);
    }
    
    interface Default extends Channel.Default {
        int BITRATE = 64000;
    }
    
    interface Bind extends Channel.Bind {
        JSONBinding.OneStage<Integer> BITRATE = identity("bitrate", JSONObject::getInteger);
    }

    interface Builder<R extends VoiceChannel, Self extends VoiceChannel.Builder> extends Channel.Builder<R, Self> {
        int getBitrate();

        Self setBitrate(int bitrate);
    }

    interface Updater<R extends VoiceChannel, Self extends VoiceChannel.Updater> extends Channel.Updater<R, Self> {
        int getBitrate();

        Self setBitrate(int bitrate);
    }
}
