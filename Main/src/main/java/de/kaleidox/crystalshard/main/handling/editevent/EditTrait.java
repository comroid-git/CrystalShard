package de.kaleidox.crystalshard.main.handling.editevent;

import de.kaleidox.crystalshard.main.handling.editevent.enums.*;
import de.kaleidox.crystalshard.main.util.Castable;

import java.util.Optional;

@SuppressWarnings("unused")
public interface EditTrait<T> extends Castable<EditTrait> {
    default Optional<ServerEditTrait> toServerEditTrait() {
        return castTo(ServerEditTrait.class);
    }

    default Optional<ChannelEditTrait> toChannelEditTrait() {
        return castTo(ChannelEditTrait.class);
    }

    default Optional<MessageEditTrait> toMessageEditTrait() {
        return castTo(MessageEditTrait.class);
    }

    default Optional<PresenceEditTrait> toPresenceEditTrait() {
        return castTo(PresenceEditTrait.class);
    }

    default Optional<RoleEditTrait> toRoleEditTrait() {
        return castTo(RoleEditTrait.class);
    }

    default Optional<ServerEmojiEditTrait> toServerEmojiEditTrait() {
        return castTo(ServerEmojiEditTrait.class);
    }

    default Optional<ServerIntegrationEditTrait> toServerIntegrationEditTrait() {
        return castTo(ServerIntegrationEditTrait.class);
    }

    default Optional<ServerMemberEditTrait> toServerMemberEditTrait() {
        return castTo(ServerMemberEditTrait.class);
    }

    default Optional<UserEditTrait> toUserEditTrait() {
        return castTo(UserEditTrait.class);
    }

    default Optional<VoiceServerEditTrait> toVoiceServerEditTrait() {
        return castTo(VoiceServerEditTrait.class);
    }

    default Optional<VoiceStateEditTrait> toVoiceStateEditTrait() {
        return castTo(VoiceStateEditTrait.class);
    }

    default Optional<WebhookEditTrait> toWebhookEditTrait() {
        return castTo(WebhookEditTrait.class);
    }
}
