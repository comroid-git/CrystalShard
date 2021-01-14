package org.comroid.crystalshard.entity.guild;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.entity.EntityType;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.model.guild.PermissionSet;
import org.comroid.crystalshard.model.guild.RoleTags;
import org.comroid.crystalshard.model.message.Mentionable;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.uniform.node.impl.StandardValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;

import java.awt.*;

public final class Role extends Snowflake.Abstract implements Mentionable {
    @RootBind
    public static final GroupBind<Role> TYPE
            = BASETYPE.rootGroup(Guild.TYPE, "role");
    public static final VarBind<Role, String, String, String> NAME
            = TYPE.createBind("name")
            .extractAs(StandardValueType.STRING)
            .asIdentities()
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<Role, Integer, Color, Color> COLOR
            = TYPE.createBind("color")
            .extractAs(StandardValueType.INTEGER)
            .andRemap(Color::new)
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<Role, Boolean, Boolean, Boolean> HOIST
            = TYPE.createBind("hoist")
            .extractAs(StandardValueType.BOOLEAN)
            .asIdentities()
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<Role, Integer, Integer, Integer> POSITION
            = TYPE.createBind("position")
            .extractAs(StandardValueType.INTEGER)
            .asIdentities()
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<Role, Integer, PermissionSet, PermissionSet> PERMISSIONS
            = TYPE.createBind("permissions")
            .extractAs(StandardValueType.INTEGER)
            .andRemap(PermissionSet::new)
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<Role, Boolean, Boolean, Boolean> MANAGED
            = TYPE.createBind("managed")
            .extractAs(StandardValueType.BOOLEAN)
            .asIdentities()
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<Role, Boolean, Boolean, Boolean> MENTIONABLE
            = TYPE.createBind("mentionable")
            .extractAs(StandardValueType.BOOLEAN)
            .asIdentities()
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<Role, UniObjectNode, RoleTags, RoleTags> TAGS
            = TYPE.createBind("tags")
            .extractAsObject()
            .andConstruct(RoleTags.TYPE)
            .onceEach()
            .build();

    public Role(ContextualProvider context, UniObjectNode data) {
        super(context, data, EntityType.ROLE);
    }
}
