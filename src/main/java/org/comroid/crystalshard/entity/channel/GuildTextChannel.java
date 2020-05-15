package org.comroid.crystalshard.entity.channel;

import org.comroid.crystalshard.DiscordBot;
import org.comroid.uniform.node.UniValueNode.ValueType;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;

import java.util.Optional;

public interface GuildTextChannel extends GuildChannel, TextChannel {
    default Optional<String> getTopic() {
        return wrap(Bind.Topic);
    }

    default boolean isNSFW() {
        return requireNonNull(Bind.Nsfw);
    }

    default int getRatelimitPerUser() {
        return requireNonNull(Bind.RatelimitPerUser);
    }

    interface Bind extends GuildChannel.Bind, TextChannel.Bind {
        @SuppressWarnings("unchecked")
        GroupBind<GuildTextChannel, DiscordBot> Root
                = GroupBind.combine("guild_text_channel", GuildChannel.Bind.Root, TextChannel.Bind.Root);
        VarBind.OneStage<String> Topic
                = Root.bind1stage("topic", ValueType.STRING);
        VarBind.OneStage<Boolean> Nsfw
                = Root.bind1stage("nsfw", ValueType.BOOLEAN);
        VarBind.OneStage<Integer> RatelimitPerUser
                = Root.bind1stage("rate_limit_per_user", ValueType.INTEGER);
    }
}
