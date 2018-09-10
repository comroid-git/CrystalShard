package de.kaleidox.crystalshard.main.handling.editevent.enums;

import de.kaleidox.crystalshard.main.handling.editevent.EditTrait;
import de.kaleidox.crystalshard.main.items.server.Server;

public enum ServerEditTrait implements EditTrait<Server> {
    NAME,

    REGION,

    VERIFICATION_LEVEL,

    DEFAULT_MESSAGE_NOTIFICATION_LEVEL,

    EXPLICIT_CONTENT_FILTER_LEVEL,

    AFK_CHANNEL,

    AFK_TIMEOUT,

    ICON,

    OWNER,

    SPLASH,

    OWN_PERMISSIONS, EMBED_ENABLED, MFA_LEVEL, WIDGET_ENABLED, LARGE, AVAILABILITY, MEMBER_COUNT, EMBED_CHANNEL, WIDGET_CHANNEL, SYSTEM_CHANNEL
}
