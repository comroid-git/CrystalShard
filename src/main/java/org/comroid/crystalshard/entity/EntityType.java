package org.comroid.crystalshard.entity;

import org.comroid.api.Named;

public enum EntityType implements Named {
    SNOWFLAKE,

    USER,
    WEBHOOK,

    GUILD,
    ROLE,
    CUSTOM_EMOJI,
    GUILD_INTEGRATION,

    CHANNEL,
    TEXT_CHANNEL,
    VOICE_CHANNEL,
    PRIVATE_CHANNEL,
    GROUP_CHANNEL,
    GUILD_CHANNEL_CATEGORY,
    GUILD_TEXT_CHANNEL,
    GUILD_VOICE_CHANNEL,

    MESSAGE,
    MESSAGE_ATTACHMENT,
    MESSAGE_APPLICATION,
    MESSAGE_STICKER,

    APPLICATION_COMMAND
}
