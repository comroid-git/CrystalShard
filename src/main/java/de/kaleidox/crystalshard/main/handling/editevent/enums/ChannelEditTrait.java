package de.kaleidox.crystalshard.main.handling.editevent.enums;

import de.kaleidox.crystalshard.main.handling.editevent.EditTrait;
import de.kaleidox.crystalshard.main.items.channel.Channel;

public enum ChannelEditTrait implements EditTrait<Channel> {
    NAME,
    POSITION,
    TOPIC,
    NSFW_FLAG,
    BITRATE,
    USER_LIMIT,
    PERMISSION_OVERWRITES,
    CATEGORY,
    PARENT_ID
}
