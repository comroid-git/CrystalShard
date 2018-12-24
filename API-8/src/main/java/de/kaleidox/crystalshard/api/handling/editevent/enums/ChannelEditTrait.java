package de.kaleidox.crystalshard.api.handling.editevent.enums;

import de.kaleidox.crystalshard.api.entity.channel.Channel;
import de.kaleidox.crystalshard.api.handling.editevent.EditTrait;

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
