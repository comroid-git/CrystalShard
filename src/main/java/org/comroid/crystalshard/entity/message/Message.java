package org.comroid.crystalshard.entity.message;

import org.comroid.common.iter.Span;
import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.entity.channel.GuildChannel;
import org.comroid.crystalshard.entity.channel.TextChannel;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.crystalshard.entity.guild.GuildMember;
import org.comroid.crystalshard.entity.guild.Role;
import org.comroid.crystalshard.entity.message.reaction.Reaction;
import org.comroid.crystalshard.entity.user.User;
import org.comroid.crystalshard.entity.webhook.Webhook;
import org.comroid.crystalshard.model.embed.Embed;
import org.comroid.crystalshard.model.emoji.Emoji;
import org.comroid.crystalshard.model.message.*;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.uniform.node.UniValueNode.ValueType;
import org.comroid.varbind.bind.ArrayBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.comroid.varbind.container.DataContainer;
import org.comroid.varbind.container.DataContainerBuilder;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

public interface Message extends Snowflake {
    ReactionBox getReactions(Emoji emoji);

    @Internal
    void clearReactions();

    final class Builder extends DataContainerBuilder<Builder, Message, DiscordBot> {
        public Builder(Class<Message> type, @Nullable DiscordBot dependencyObject) {
            super(type, dependencyObject);
        }

        @Override
        protected Message mergeVarCarrier(DataContainer<DiscordBot> dataContainer) {
            return null; // todo
        }
    }

    interface Bind extends Snowflake.Bind {
        GroupBind<Message, DiscordBot> Root = Snowflake.Bind.Root.subGroup("message");
        VarBind.DependentTwoStage<Long, DiscordBot, TextChannel> Channel
                = Root.bindDependent("channel_id", ValueType.LONG, (bot, id) -> bot.getTextChannelByID(id).requireNonNull());
        VarBind.DependentTwoStage<Long, DiscordBot, Guild> Guild
                = Root.bindDependent("guild_id", ValueType.LONG, (bot, id) -> bot.getGuildByID(id).get());
        VarBind.DependentTwoStage<UniObjectNode, DiscordBot, User> Author
                = Root.bindDependent("author", DiscordBot::updateUser);
        VarBind.DependentTwoStage<UniObjectNode, DiscordBot, GuildMember> AuthorMember
                = Root.bindDependent("member", DiscordBot::updateGuildMember);
        VarBind.OneStage<String> Content
                = Root.bind1stage("content", ValueType.STRING);
        VarBind.TwoStage<String, Instant> SentTimestamp
                = Root.bind2stage("timestamp", ValueType.STRING, Instant::parse);
        VarBind.TwoStage<String, Instant> EditedTimestamp
                = Root.bind2stage("edited_timestamp", ValueType.STRING, Instant::parse);
        VarBind.OneStage<Boolean> TTS
                = Root.bind1stage("tts", ValueType.BOOLEAN);
        VarBind.DependentTwoStage<UniObjectNode, DiscordBot, MessageMentions> AllowedMentions
                = Root.bindDependent("allowed_mentions", MessageMentions.Bind.Root);
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
        ArrayBind.DependentTwoStage<UniObjectNode, DiscordBot, Embed, Span<Embed>> Embeds
                = Root.listDependent("embeds", Embed.Bind.Root, Span::new);
        ArrayBind.DependentTwoStage<UniObjectNode, DiscordBot, Reaction, Collection<Reaction>> Reactions
                = Root.listDependent("reactions", Reaction.Bind.Root, ArrayList::new);
        VarBind.OneStage<String> Nonce
                = Root.bind1stage("nonce", ValueType.STRING);
        VarBind.OneStage<Boolean> Pinned
                = Root.bind1stage("pinned", ValueType.BOOLEAN);
        VarBind.DependentTwoStage<Long, DiscordBot, Webhook> AuthorWebhook
                = Root.bindDependent("webhook_id", ValueType.LONG, (bot, id) -> bot.getWebhookByID(id).get());
        VarBind.TwoStage<Integer, MessageType> Type
                = Root.bind2stage("type", ValueType.INTEGER, MessageType::valueOf);
        VarBind.DependentTwoStage<UniObjectNode, DiscordBot, MessageActivity> Activity
                = Root.bindDependent("activity", (bot, data) -> bot.resolveMessageActivity(data).get());
        VarBind.DependentTwoStage<UniObjectNode, DiscordBot, MessageApplication> Application
                = Root.bindDependent("application", (bot, data) -> bot.resolveMessageApplication(data).get());
        VarBind.DependentTwoStage<UniObjectNode, DiscordBot, MessageReference> Reference
                = Root.bindDependent("message_reference", (bot, data) -> bot.resolveMessageReference(data).get());
        VarBind.TwoStage<Integer, Set<MessageFlag>> Flags
                = Root.bind2stage("flags", ValueType.INTEGER, MessageFlag::valueOf);
    }
}
