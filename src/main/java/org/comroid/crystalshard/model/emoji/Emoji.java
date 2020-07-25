package org.comroid.crystalshard.model.emoji;

import org.comroid.common.info.MessageSupplier;
import org.comroid.common.ref.Named;
import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.entity.guild.CustomEmoji;
import org.comroid.uniform.node.UniNode;
import org.comroid.uniform.node.UniObjectNode;
import org.jetbrains.annotations.ApiStatus.Internal;

public interface Emoji extends Named {
    @Internal
    static Emoji find(UniObjectNode data, DiscordBot bot) {
        return data.process("id")
                .map(UniNode::asLong)
                .<Emoji>flatMap(id -> bot.getSnowflake(Snowflake.Type.CUSTOM_EMOJI, id)
                        .peek(emoji -> emoji.updateFrom(data))
                        .or(() -> new CustomEmoji(bot, data)))
                .or(data.process("name")
                        .map(UniNode::asString)
                        .map(UnicodeEmoji::of))
                .requireNonNull(MessageSupplier.format("Could not find Emoji for data: %s", data));
    }
}
