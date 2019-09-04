package de.kaleidox.crystalshard.api.model.permission;

public enum Permission {
    // general
    CREATE_INSTANT_INVITE(0x00000001),
    KICK_MEMBERS(0x00000002),
    BAN_MEMBERS(0x00000004),
    ADMINISTRATOR(0x00000008),
    MANAGE_CHANNELS(0x00000010),
    MANAGE_SERVER(0x00000020),
    ADD_REACTIONS(0x00000040),
    VIEW_AUDIT_LOG(0x00000080),

    // chat
    READ_MESSAGES(0x00000400),
    SEND_MESSAGES(0x00000800),
    SEND_TTS_MESSAGES(0x00001000),
    MANAGE_MESSAGES(0x00002000),
    EMBED_LINKS(0x00004000),
    ATTACH_FILE(0x00008000),
    READ_MESSAGE_HISTORY(0x00010000),
    MENTION_EVERYONE(0x00020000),
    USE_EXTERNAL_EMOJIS(0x00040000),

    // voice
    CONNECT(0x00100000),
    SPEAK(0x00200000),
    MUTE_MEMBERS(0x00400000),
    DEAFEN_MEMBERS(0x00800000),
    MOVE_MEMBERS(0x01000000),
    USE_VOICE_ACTIVITY(0x02000000),
    PRIORITY_SPEAKER(0x00000100),

    // misc
    CHANGE_NICKNAME(0x04000000),
    MANAGE_NICKNAMES(0x08000000),
    MANAGE_ROLES(0x10000000),
    MANAGE_WEBHOOKS(0x20000000),
    MANAGE_EMOJIS(0x40000000);

    public final int value;

    Permission(int value) {
        this.value = value;
    }

    public boolean isSet(int i) {
        return (i & value) != 0;
    }

    public int apply(int i, boolean set) {
        if (set && !isSet(i)) {
            return i + value;
        }
        if (!set && isSet(i)) {
            return i - value;
        }
        return i;
    }
}
