package org.comroid.crystalshard.core.gateway.payload.voice;

import org.comroid.api.Polyfill;
import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.core.gateway.event.GatewayPayloadWrapper;
import org.comroid.crystalshard.core.gateway.payload.AbstractGatewayPayload;
import org.comroid.crystalshard.entity.DiscordEntity;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.uniform.ValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;

import java.net.URI;

public final class GatewayVoiceServerUpdatePayload extends AbstractGatewayPayload {
    @RootBind
    public static final GroupBind<GatewayVoiceServerUpdatePayload, DiscordBot> Root
            = BaseGroup.rootGroup("gateway-voice-server-update");
    public static final VarBind<Object, String, String, String> connectionToken
            = Root.createBind("token")
            .extractAs(ValueType.STRING)
            .asIdentities()
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<Object, Long, Guild, Guild> guild
            = Root.createBind("guild_id")
            .extractAs(ValueType.LONG)
            .andResolve((id, bot) -> bot.getSnowflake(DiscordEntity.Type.GUILD, id)
                    .requireNonNull("Guild not found"))
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<Object, String, URI, URI> host
            = Root.createBind("endpoint")
            .extractAs(ValueType.STRING)
            .andRemap(Polyfill::uri)
            .onceEach()
            .setRequired()
            .build();

    public GatewayVoiceServerUpdatePayload(GatewayPayloadWrapper gpw) {
        super(gpw);
    }
}
