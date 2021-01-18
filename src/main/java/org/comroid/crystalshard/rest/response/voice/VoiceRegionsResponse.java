package org.comroid.crystalshard.rest.response.voice;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.rest.response.AbstractRestResponse;
import org.comroid.crystalshard.model.voice.VoiceRegion;
import org.comroid.mutatio.ref.Reference;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public final class VoiceRegionsResponse extends AbstractRestResponse {
    @RootBind
    public static final GroupBind<VoiceRegionsResponse> TYPE
            = BASETYPE.rootGroup("voice-regions-response");
    public static final VarBind<VoiceRegionsResponse, UniObjectNode, VoiceRegion, ArrayList<VoiceRegion>> REGIONS
            = TYPE.createBind("")
            .extractAsArray()
            .andResolve(VoiceRegion::find)
            .intoCollection(ArrayList::new)
            .build();
    public final Reference<ArrayList<VoiceRegion>> regions = getComputedReference(REGIONS);

    public VoiceRegionsResponse(ContextualProvider context, @Nullable UniObjectNode initialData) {
        super(context, initialData);
    }
}
