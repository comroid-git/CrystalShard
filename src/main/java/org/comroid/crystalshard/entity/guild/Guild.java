package org.comroid.crystalshard.entity.guild;

import org.comroid.api.ContextualProvider;
import org.comroid.api.Named;
import org.comroid.crystalshard.SnowflakeCache;
import org.comroid.crystalshard.entity.EntityType;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.entity.channel.Channel;
import org.comroid.crystalshard.entity.user.User;
import org.comroid.crystalshard.voice.VoiceRegion;
import org.comroid.uniform.ValueType;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;

public final class Guild extends Snowflake.Abstract implements Named {
    public static final GroupBind<Guild> TYPE = BASETYPE.rootGroup("guild");
    public static final VarBind<Guild, String, String, String> ICON_HASH
            = TYPE.createBind("icon")
            .extractAs(ValueType.STRING)
            .asIdentities()
            .onceEach()
            .build();
    public static final VarBind<Guild, String, String, String> SPLASH_HASH
            = TYPE.createBind("splash")
            .extractAs(ValueType.STRING)
            .asIdentities()
            .onceEach()
            .build();
    public static final VarBind<Guild, String, String, String> DISCOVERY_SPLASH_HASH
            = TYPE.createBind("discovery_splash_hash")
            .extractAs(ValueType.STRING)
            .asIdentities()
            .onceEach()
            .build();
    public static final VarBind<Guild, Boolean, Boolean, Boolean> IS_OWNER
            = TYPE.createBind("owner")
            .extractAs(ValueType.BOOLEAN)
            .asIdentities()
            .onceEach()
            .build();
    public static final VarBind<Guild, Long, User, User> OWNER
            = TYPE.createBind("owner_id")
            .extractAs(ValueType.LONG)
            .andResolve((guild, owner) -> guild.requireFromContext(SnowflakeCache.class)
                    .getUser(owner).get())
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<Guild, String, VoiceRegion, VoiceRegion> VOICE_REGION
            = TYPE.createBind("region")
            .extractAs(ValueType.STRING)
            .andResolve(VoiceRegion::find)
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<Guild, Long, Channel, Channel> AFK_CHANNEL
            = TYPE.createBind("afk_channel_id")
            .extractAs(ValueType.LONG)
            .andResolve((guild, channel) -> guild.requireFromContext(SnowflakeCache.class)
                    .getChannel(channel).get())
            .onceEach()
            .build();
    public static final VarBind<Guild, Integer, Integer, Integer> AFK_TIMEOUT
            = TYPE.createBind("afk_timeout")
            .extractAs(ValueType.INTEGER)
            .asIdentities()
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<Guild, Boolean, Boolean, Boolean> IS_WIDGET_ENABLED
            = TYPE.createBind("widget_enabled")
            .extractAs(ValueType.BOOLEAN)
            .asIdentities()
            .onceEach()
            .build();
    public static final VarBind<Guild, Long, Channel, Channel> WIDGET_CHANNEL_ID
            = TYPE.createBind("widget_channel_id")
            .extractAs(ValueType.LONG)
            .andResolve((guild, channel) -> guild.requireFromContext(SnowflakeCache.class)
                    .getChannel(channel).get())
            .onceEach()
            .build();
    

    @Override
    public String getName() {
        return null;
    }

    public Guild(ContextualProvider context, UniObjectNode data) {
        super(context, data, EntityType.GUILD);
    }
}
