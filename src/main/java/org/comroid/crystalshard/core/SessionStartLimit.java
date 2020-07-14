package org.comroid.crystalshard.core;

import org.comroid.api.Invocable;
import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.model.BotBound;
import org.comroid.uniform.ValueType;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.annotation.Location;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;

@Location(SessionStartLimit.class)
public final class SessionStartLimit extends BotBound.DataBase {
    @RootBind
    public static final GroupBind<SessionStartLimit, DiscordBot> Root
            = BaseGroup.subGroup("session-start-limit", Invocable.ofConstructor(SessionStartLimit.class));
    public static final VarBind<Integer, DiscordBot, Integer, Integer> TotalAllowed
            = Root.createBind("total")
            .extractAs(ValueType.INTEGER)
            .asIdentities()
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<Integer, DiscordBot, Integer, Integer> RemainingAllowed
            = Root.createBind("remaining")
            .extractAs(ValueType.INTEGER)
            .asIdentities()
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<Integer, DiscordBot, Integer, Integer> ResetAfter
            = Root.createBind("reset_after")
            .extractAs(ValueType.INTEGER)
            .asIdentities()
            .onceEach()
            .setRequired()
            .build();

    public int getTotalAllowed() {
        return requireNonNull(TotalAllowed);
    }

    public int getRemainingAllowed() {
        return requireNonNull(RemainingAllowed);
    }

    public int getResetAfter() {
        return requireNonNull(ResetAfter);
    }

    public SessionStartLimit(
            DiscordBot bot,
            UniObjectNode initialData
    ) {
        super(bot, initialData);
    }
}
