package org.comroid.crystalshard.gateway.event.dispatch.guild.emoji;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.entity.guild.CustomEmoji;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.crystalshard.gateway.event.dispatch.DispatchEvent;
import org.comroid.mutatio.ref.Reference;
import org.comroid.mutatio.span.Span;
import org.comroid.uniform.node.UniNode;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.util.StandardValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.Nullable;

public final class GuildEmojisUpdateEvent extends DispatchEvent {
    @RootBind
    public static final GroupBind<GuildEmojisUpdateEvent> TYPE
            = BASETYPE.subGroup("guild-emojis-update", GuildEmojisUpdateEvent::new);
    public static final VarBind<GuildEmojisUpdateEvent, Long, Guild, Guild> GUILD
            = TYPE.createBind("guild_id")
            .extractAs(StandardValueType.LONG)
            .andResolveRef((event, id) -> event.getCache().getGuild(id))
            .build();
    public static final VarBind<GuildEmojisUpdateEvent, UniObjectNode, CustomEmoji, Span<CustomEmoji>> EMOJIS
            = TYPE.createBind("emojis")
            .extractAsArray()
            .andResolve(CustomEmoji::resolve)
            .intoSpan()
            .build();
    public final Reference<Guild> guild = getComputedReference(GUILD);
    public final Reference<Span<CustomEmoji>> emojis = getComputedReference(EMOJIS);

    public Guild getGuild() {
        return guild.assertion();
    }

    public Span<CustomEmoji> getEmojis() {
        return emojis.orElseGet(Span::empty);
    }

    public GuildEmojisUpdateEvent(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);
    }
}
