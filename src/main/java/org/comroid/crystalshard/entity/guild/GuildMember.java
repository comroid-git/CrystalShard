package org.comroid.crystalshard.entity.guild;

import org.comroid.crystalshard.CrystalShard;
import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.entity.user.User;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.annotation.Location;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.ReBind;
import org.comroid.varbind.bind.VarBind;

@Location(GuildMember.Bind.class)
public interface GuildMember extends User {
    Guild getGuild();

    User getUser();

    interface Bind {
        @RootBind
        GroupBind<GuildMember, DiscordBot> Root
                = new GroupBind<>(CrystalShard.SERIALIZATION_ADAPTER, "guild_member");
        VarBind.DependentTwoStage<UniObjectNode, DiscordBot, User> UnderlyingUser
                = Root.bindDependent("user", (bot, data) -> bot.getCache().autoUpdate(User.class, data).get());
        ReBind.TwoStage<User, Long> ID
                = UnderlyingUser.rebindSimple(Snowflake::getID);
    }
}
