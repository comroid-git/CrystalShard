package org.comroid.crystalshard.rest.response.message;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.rest.response.AbstractRestResponse;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.jetbrains.annotations.Nullable;

public class SendMessageResponse extends AbstractRestResponse {
    @RootBind
    public static final GroupBind<SendMessageResponse> TYPE
            = BASETYPE.rootGroup("rest-send-message-response");

    protected SendMessageResponse(ContextualProvider context, @Nullable UniObjectNode initialData) {
        super(context, initialData);
    }
}
