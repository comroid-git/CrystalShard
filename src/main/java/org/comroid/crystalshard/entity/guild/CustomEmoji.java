package org.comroid.crystalshard.entity.guild;

import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.model.BotBound;
import org.comroid.crystalshard.model.emoji.Emoji;
import org.comroid.uniform.node.UniObjectNode;

public final class CustomEmoji extends BotBound.DataBase implements Emoji, Snowflake {
    @Override
    public String getName() {
        return null;
    }

    @Override
    public Type<? extends Snowflake> getType() {
        return Type.CUSTOM_EMOJI;
    }

    public CustomEmoji(DiscordBot bot, UniObjectNode initialData) {
        super(bot, initialData);
    }
}
