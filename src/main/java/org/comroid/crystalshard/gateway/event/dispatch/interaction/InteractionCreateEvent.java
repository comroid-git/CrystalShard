package org.comroid.crystalshard.gateway.event.dispatch.interaction;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.gateway.event.dispatch.DispatchEvent;
import org.comroid.crystalshard.ui.Interaction;
import org.comroid.mutatio.ref.Reference;
import org.comroid.uniform.node.UniNode;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.Nullable;

public final class InteractionCreateEvent extends DispatchEvent {
    @RootBind
    public static final GroupBind<InteractionCreateEvent> TYPE
            = BASETYPE.subGroup("interaction-create", InteractionCreateEvent::new);
    public static final VarBind<InteractionCreateEvent, UniObjectNode, Interaction, Interaction> INTERACTION
            = TYPE.createBind("")
            .extractAsObject()
            .andConstruct(Interaction.TYPE)
            .build();
    public final Reference<Interaction> interaction = getComputedReference(INTERACTION);

    public Interaction getInteraction() {
        return interaction.assertion();
    }

    public InteractionCreateEvent(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);
    }
}
