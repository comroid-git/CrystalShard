package de.kaleidox.crystalshard.main.event.types;

public enum MessageAttachingEvent implements AttachingEvent {
    MESSAGE_GENERIC,

    MESSAGE_CREATE,

    MESSAGE_DELETE,

    MESSAGE_EDIT,

    MESSAGE_PIN,

    REACTION_ADD,

    REACTION_REMOVE,

    REACTION_REMOVEALL
}
