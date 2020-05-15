package org.comroid.crystalshard.entity.channel;

import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.entity.channel.Channel;
import org.comroid.crystalshard.model.Mentionable;
import org.comroid.uniform.node.UniValueNode;
import org.comroid.uniform.node.UniValueNode.ValueType;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;

import java.util.Optional;

public interface VoiceChannel extends Channel {
    default int getBitrate() {
        return requireNonNull(Bind.Bitrate);
    }

    default Optional<Integer> getUserLimit() {
        return wrap(Bind.UserLimit);
    }

    interface Bind extends Channel.Bind {
        GroupBind<VoiceChannel, DiscordBot> Root
                = Channel.Bind.Root.subGroup("voice_channel");
        VarBind.OneStage<Integer> Bitrate
                = Root.bind1stage("bitrate", ValueType.INTEGER);
        VarBind.OneStage<Integer> UserLimit
                = Root.bind1stage("user_limit", ValueType.INTEGER);
    }
}
