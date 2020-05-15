package org.comroid.crystalshard.entity.message;

import org.comroid.common.iter.Span;
import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.entity.channel.Channel;
import org.comroid.crystalshard.entity.channel.GuildChannel;
import org.comroid.crystalshard.entity.channel.TextChannel;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.crystalshard.entity.guild.GuildMember;
import org.comroid.crystalshard.entity.guild.Role;
import org.comroid.crystalshard.entity.user.User;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.uniform.node.UniValueNode.ValueType;
import org.comroid.varbind.bind.ArrayBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;

public interface Message extends Snowflake {
    interface Bind extends Snowflake.Bind {
        GroupBind<Message, DiscordBot> Root = Snowflake.Bind.Root.subGroup("message");
        VarBind.DependentTwoStage<Long, DiscordBot, TextChannel> Channel
                = Root.bindDependent("channel_id", ValueType.LONG, (bot, id) -> bot.getTextChannelByID(id).requireNonNull());
        VarBind.DependentTwoStage<Long, DiscordBot, Guild> Guild
                = Root.bindDependent("guild_id", ValueType.LONG, (bot, id) -> bot.getGuildByID(id).get());
        VarBind.DependentTwoStage<UniObjectNode, DiscordBot, User> Author
                = Root.bindDependent("author", DiscordBot::updateUser);
        VarBind.DependentTwoStage<UniObjectNode, DiscordBot, GuildMember.Partial> AuthorMember
                = Root.bindDependent("member", DiscordBot::updateGuildMember);
        VarBind.OneStage<String> Content
                = Root.bind1stage("content", ValueType.STRING);
        VarBind.TwoStage<String, Instant> SentTimestamp
                = Root.bind2stage("timestamp", ValueType.STRING, Instant::parse);
        VarBind.TwoStage<String, Instant> EditedTimestamp
                = Root.bind2stage("edited_timestamp", ValueType.STRING, Instant::parse);
        VarBind.OneStage<Boolean> TTS
                = Root.bind1stage("tts", ValueType.BOOLEAN);
        VarBind.OneStage<Boolean> MentionsEveryone
                = Root.bind1stage("mention_everyone", ValueType.BOOLEAN);
        ArrayBind.DependentTwoStage<UniObjectNode, DiscordBot, User, Collection<User>> MentionedUsers
                = Root.listDependent("mentions", DiscordBot::updateUser, ArrayList::new);
        ArrayBind.DependentTwoStage<Long, DiscordBot, Role, Collection<Role>> MentionedRoles
                = Root.listDependent("mention_roles", ValueType.LONG, (bot, id) -> bot.getRoleByID(id).get(), ArrayList::new);
        ArrayBind.DependentTwoStage<UniObjectNode, DiscordBot, GuildChannel, Collection<GuildChannel>> MentionedChannels
                = Root.listDependent("mention_channels", (bot, data) -> bot.resolveChannelMention(data)
                .flatMap(org.comroid.crystalshard.entity.channel.Channel::asGuildChannel).get(), ArrayList::new);
        ArrayBind.DependentTwoStage<UniObjectNode, DiscordBot, MessageAttachment, Collection<MessageAttachment>> Attachments
                = Root.listDependent("attachments", MessageAttachment.Bind.Root, ArrayList::new);
        ArrayBind.DependentTwoStage<UniObjectNode, DiscordBot, MessageEmbed, Span<MessageEmbed>> Embeds
                = Root.listDependent("embeds", MessageEmbed.Bind.Root, Span::new);
        //todo incomplete
    }
}
