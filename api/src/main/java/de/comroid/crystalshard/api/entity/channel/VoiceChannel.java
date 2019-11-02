package de.comroid.crystalshard.api.entity.channel;

import de.comroid.crystalshard.util.annotation.IntroducedBy;
import de.comroid.crystalshard.util.model.serialization.JsonBinding;

import com.fasterxml.jackson.databind.JsonNode;

import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.GETTER;
import static de.comroid.crystalshard.util.model.serialization.JsonBinding.identity;

public interface VoiceChannel extends Channel {
    @IntroducedBy(GETTER)
    default int getBitrate() {
        return wrapTraitValue(Trait.BITRATE).orElse(VoiceChannel.Default.BITRATE);
    }
    
    interface Default extends Channel.Default {
        int BITRATE = 64000;
    }
    
    interface Trait extends Channel.Trait {
        JsonBinding<Integer, Integer> BITRATE = identity(JsonNode::asInt, "bitrate");
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
