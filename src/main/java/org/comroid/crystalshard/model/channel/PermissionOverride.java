package org.comroid.crystalshard.model.channel;

import org.comroid.common.func.Processor;
import org.comroid.crystalshard.CrystalShard;
import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.entity.guild.Role;
import org.comroid.crystalshard.entity.user.User;
import org.comroid.crystalshard.model.BotBound;
import org.comroid.crystalshard.model.permission.PermissionSet;
import org.comroid.uniform.node.UniValueNode.ValueType;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.comroid.varbind.container.DataContainer;

public interface PermissionOverride extends DataContainer<DiscordBot>, BotBound {
    default <O extends Snowflake & Settable> Processor<O> getRelated(Class<O> asType) {
        return ref(Bind.RelatedId)
                .process()
                .flatMap(id -> getBot().getSnowflake(id).wrap())
                .filter(asType::isInstance)
                .map(asType::cast);
    }

    default Processor<Role> processRole() {
        return getRelated(Role.class);
    }

    default boolean isRoleRelated() {
        return processRole().isPresent();
    }

    default Processor<User> processUser() {
        return getRelated(User.class);
    }

    default boolean isUserRelated() {
        return processUser().isPresent();
    }

    interface Settable {
    }

    interface Bind {
        GroupBind<PermissionOverride, DiscordBot> Root = new GroupBind<>(CrystalShard.SERIALIZATION_ADAPTER, "permission_override");
        VarBind.OneStage<Long> RelatedId = Root.bind1stage("id", ValueType.LONG);
        VarBind.OneStage<String> Type = Root.bind1stage("type", ValueType.STRING);
        VarBind.TwoStage<Integer, PermissionSet> AllowedPermissions = Root.bind2stage("allow", ValueType.INTEGER, PermissionSet::ofMask);
        VarBind.TwoStage<Integer, PermissionSet> DeniedPermissions = Root.bind2stage("deny", ValueType.INTEGER, PermissionSet::ofMask);
    }
}
