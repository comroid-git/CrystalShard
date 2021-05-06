package org.comroid.crystalshard.gateway.event.dispatch.voice;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.gateway.event.dispatch.DispatchEvent;
import org.comroid.uniform.node.UniNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.jetbrains.annotations.Nullable;

public final class VoiceServerUpdateEvent extends DispatchEvent {
    @RootBind
    public static final GroupBind<VoiceServerUpdateEvent> TYPE
            = BASETYPE.subGroup("voice-server-update", VoiceServerUpdateEvent::new);

    public VoiceServerUpdateEvent(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);

        // todo implement Voice functionality
    }
}
