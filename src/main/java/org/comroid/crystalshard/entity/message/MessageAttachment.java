package org.comroid.crystalshard.entity.message;

import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.varbind.bind.GroupBind;

public interface MessageAttachment extends Snowflake {
    interface Bind extends Snowflake.Bind {
        GroupBind<MessageAttachment, DiscordBot> Root = Snowflake.Bind.Root.subGroup("message_attachment");
    }
}
