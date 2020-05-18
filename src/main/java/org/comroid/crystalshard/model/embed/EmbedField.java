package org.comroid.crystalshard.model.embed;

import org.comroid.common.info.Described;
import org.comroid.common.ref.Named;
import org.comroid.crystalshard.CrystalShard;
import org.comroid.crystalshard.DiscordBot;
import org.comroid.uniform.node.UniValueNode;
import org.comroid.uniform.node.UniValueNode.ValueType;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;

public interface EmbedField extends Named, Described, EmbedMember {
    interface Bind {
        GroupBind<EmbedField, DiscordBot> Root
                = new GroupBind<>(CrystalShard.SERIALIZATION_ADAPTER, "embed_field");
        VarBind.OneStage<String> Name
                = Root.bind1stage("name", ValueType.STRING);
        VarBind.OneStage<String> Value
                = Root.bind1stage("value", ValueType.STRING);
        VarBind.OneStage<Boolean> Inline
                = Root.bind1stage("inline", ValueType.BOOLEAN);
    }
}
