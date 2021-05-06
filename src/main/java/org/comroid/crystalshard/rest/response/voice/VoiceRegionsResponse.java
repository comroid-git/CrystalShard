package org.comroid.crystalshard.rest.response.voice;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.model.voice.VoiceRegion;
import org.comroid.crystalshard.rest.response.AbstractRestResponse;
import org.comroid.mutatio.ref.Reference;
import org.comroid.uniform.node.UniNode;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.function.Supplier;

public final class VoiceRegionsResponse extends AbstractRestResponse {
    @RootBind
    public static final GroupBind<VoiceRegionsResponse> TYPE
            = BASETYPE.subGroup("voice-regions-response", VoiceRegionsResponse::new);
    public static final VarBind<VoiceRegionsResponse, UniObjectNode, VoiceRegion, ArrayList<VoiceRegion>> REGIONS
            = TYPE.createBind("")
            .extractAsArray()
            .andResolve(VoiceRegion::find)
            .intoCollection((Supplier<ArrayList<VoiceRegion>>) ArrayList::new)
            .build();
    public final Reference<ArrayList<VoiceRegion>> regions = getComputedReference(REGIONS);

    public VoiceRegionsResponse(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);
    }
}
