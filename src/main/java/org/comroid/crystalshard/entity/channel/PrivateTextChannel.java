package org.comroid.crystalshard.entity.channel;

import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.entity.user.User;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.bind.ArrayBind;
import org.comroid.varbind.bind.GroupBind;

import java.util.ArrayList;
import java.util.Collection;

public interface PrivateTextChannel extends PrivateChannel, TextChannel {
    default Collection<User> getRecipients() {
        return requireNonNull(Bind.Recipients);
    }

    interface Bind extends PrivateChannel.Bind, TextChannel.Bind {
        @SuppressWarnings("unchecked")
        GroupBind<PrivateTextChannel, DiscordBot> Root
                = GroupBind.combine("private_text_channel", PrivateChannel.Bind.Root, TextChannel.Bind.Root);
        ArrayBind.DependentTwoStage<UniObjectNode, DiscordBot, User, Collection<User>> Recipients
                = Root.listDependent("recipients", DiscordBot::updateUser, ArrayList::new);
    }
}
