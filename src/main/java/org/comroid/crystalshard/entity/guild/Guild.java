package org.comroid.crystalshard.entity.guild;

import org.comroid.common.Polyfill;
import org.comroid.common.ref.Named;
import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.entity.channel.GuildChannel;
import org.comroid.crystalshard.entity.channel.GuildTextChannel;
import org.comroid.crystalshard.entity.channel.GuildVoiceChannel;
import org.comroid.crystalshard.entity.guild.emoji.CustomEmoji;
import org.comroid.crystalshard.entity.user.User;
import org.comroid.crystalshard.model.guild.*;
import org.comroid.crystalshard.model.permission.PermissionSet;
import org.comroid.crystalshard.model.user.UserPresence;
import org.comroid.crystalshard.voice.VoiceRegion;
import org.comroid.crystalshard.voice.VoiceState;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.uniform.node.UniValueNode;
import org.comroid.varbind.annotation.Location;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.ArrayBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URL;
import java.time.Instant;
import java.util.*;

@Location(Guild.Bind.class)
public interface Guild extends Snowflake, Named {
    @Override
    default String getName() {
        return requireNonNull(Bind.Name);
    }

    interface Bind extends Snowflake.Bind {
        @RootBind
        GroupBind<Guild, DiscordBot> Root
                = Snowflake.Bind.Root.<Guild>subGroup("guild", Guild.Basic.class);
        VarBind.OneStage<String> Name
                = Root.bind1stage("name", UniValueNode.ValueType.STRING);
        VarBind.OneStage<String> IconHash
                = Root.bind1stage("icon", UniValueNode.ValueType.STRING);
        VarBind.OneStage<String> Splash
                = Root.bind1stage("splash", UniValueNode.ValueType.STRING);
        VarBind.OneStage<String> DiscoverySplash
                = Root.bind1stage("discovery_splash", UniValueNode.ValueType.STRING);
        VarBind.OneStage<Boolean> AreYouOwner
                = Root.bind1stage("owner", UniValueNode.ValueType.BOOLEAN);
        VarBind.DependentTwoStage<Long, DiscordBot, User> Owner
                = Root.bindDependent("owner_id", UniValueNode.ValueType.LONG, (bot, id) -> bot.getUserByID(id).requireNonNull());
        VarBind.TwoStage<Integer, PermissionSet> YourPermissions
                = Root.bind2stage("permissions", UniValueNode.ValueType.INTEGER, PermissionSet::ofMask);
        VarBind.TwoStage<String, VoiceRegion> Region
                = Root.bind2stage("region", UniValueNode.ValueType.STRING, VoiceRegion::valueOf);
        VarBind.DependentTwoStage<Long, DiscordBot, GuildVoiceChannel> AfkChannel
                = Root.bindDependent("afk_channel_id", UniValueNode.ValueType.LONG, (bot, id) -> bot.getGuildVoiceChannelByID(id).get());
        VarBind.OneStage<Integer> AfkTimeout
                = Root.bind1stage("afk_timeout", UniValueNode.ValueType.INTEGER);
        VarBind.OneStage<Boolean> GuildEmbedEnabled
                = Root.bind1stage("embed_enabled", UniValueNode.ValueType.BOOLEAN);
        VarBind.DependentTwoStage<Long, DiscordBot, ? extends GuildChannel> GuildEmbedChannel
                = Root.bindDependent("embed_channel_id", UniValueNode.ValueType.LONG, (bot, id) -> bot.getGuildChannelByID(id).get());
        VarBind.TwoStage<Integer, GuildVerificationLevel> VerificationLevel
                = Root.bind2stage("verification_level", UniValueNode.ValueType.INTEGER, GuildVerificationLevel::valueOf);
        VarBind.TwoStage<Integer, DefaultMessageNotificationLevel> DefaultMessageNotifications
                = Root.bind2stage("default_message_notifications", UniValueNode.ValueType.INTEGER, DefaultMessageNotificationLevel::valueOf);
        VarBind.TwoStage<Integer, ExplicitContentFilter> ExplicitContentFilterLevel
                = Root.bind2stage("explicit_content_filter", UniValueNode.ValueType.INTEGER, ExplicitContentFilter::valueOf);
        ArrayBind.DependentTwoStage<UniObjectNode, DiscordBot, Role, List<Role>> Roles
                = Root.listDependent("roles", (bot, data) -> bot.getEntityCache().autoUpdate(Role.class, data).get(), ArrayList::new);
        ArrayBind.DependentTwoStage<UniObjectNode, DiscordBot, CustomEmoji, List<CustomEmoji>> Emojis
                = Root.listDependent("emojis", (bot, data) -> bot.getEntityCache().autoUpdate(CustomEmoji.class, data).get(), ArrayList::new);
        ArrayBind.TwoStage<String, GuildFeature, List<GuildFeature>> Features
                = Root.list2stage("features", UniValueNode.ValueType.STRING, GuildFeature::valueOf, ArrayList::new);
        VarBind.TwoStage<Integer, MFALevel> MfaLevel
                = Root.bind2stage("mfa_level", UniValueNode.ValueType.INTEGER, MFALevel::valueOf);
        VarBind.DependentTwoStage<Long, DiscordBot, ? extends Snowflake> OwnerApplication
                = Root.bindDependent("application_id", UniValueNode.ValueType.LONG, (bot, id) -> bot.getSnowflakeByID(id).get());
        VarBind.OneStage<Boolean> WidgetEnabled
                = Root.bind1stage("widget_enabled", UniValueNode.ValueType.BOOLEAN);
        VarBind.DependentTwoStage<Long, DiscordBot, ? extends GuildChannel> WidgetChannel
                = Root.bindDependent("widget_channel_id", UniValueNode.ValueType.LONG, (bot, id) -> bot.getGuildChannelByID(id).get());
        VarBind.DependentTwoStage<Long, DiscordBot, ? extends GuildChannel> SystemChannel
                = Root.bindDependent("system_channel_id", UniValueNode.ValueType.LONG, (bot, id) -> bot.getGuildChannelByID(id).get());
        ArrayBind.TwoStage<Integer, GuildSystemChannelFlag, Set<GuildSystemChannelFlag>> SystemChannelFlags
                = Root.list2stage("system_channel_flags", UniValueNode.ValueType.INTEGER, GuildSystemChannelFlag::valueOf, HashSet::new);
        VarBind.DependentTwoStage<Long, DiscordBot, GuildTextChannel> RulesChannel
                = Root.bindDependent("rules_channel_id", UniValueNode.ValueType.LONG, (bot, id) -> bot.getGuildTextChannelByID(id).get());
        VarBind.TwoStage<String, Instant> YouJoinedAt
                = Root.bind2stage("joined_at", UniValueNode.ValueType.STRING, Instant::parse);
        VarBind.OneStage<Boolean> Large
                = Root.bind1stage("large", UniValueNode.ValueType.BOOLEAN);
        VarBind.OneStage<Boolean> Unavailable
                = Root.bind1stage("unavailable", UniValueNode.ValueType.BOOLEAN);
        VarBind.OneStage<Integer> MemberCount
                = Root.bind1stage("member_count", UniValueNode.ValueType.INTEGER);
        ArrayBind.DependentTwoStage<UniObjectNode, DiscordBot, VoiceState, Set<VoiceState>> VoiceStates
                = Root.listDependent("voice_states", DiscordBot::updateVoiceState, HashSet::new);
        ArrayBind.DependentTwoStage<UniObjectNode, DiscordBot, GuildMember, Set<GuildMember>> Members
                = Root.listDependent("members", (bot, data) -> bot.getEntityCache().autoUpdate(GuildMember.Bind.Root, data).get(), HashSet::new);
        ArrayBind.DependentTwoStage<UniObjectNode, DiscordBot, ? extends GuildChannel, Set<? extends GuildChannel>> Channels
                = Root.listDependent("channels", (bot, data) -> bot.getEntityCache().autoUpdate(GuildChannel.Bind.Root, data).get(), HashSet::new);
        ArrayBind.DependentTwoStage<UniObjectNode, DiscordBot, UserPresence, Set<UserPresence>> Presences
                = Root.listDependent("presences", DiscordBot::updatePresence, HashSet::new);
        VarBind.OneStage<Integer> MaxPresences
                = Root.bind1stage("max_presences", UniValueNode.ValueType.INTEGER);
        VarBind.OneStage<Integer> MaxMembers
                = Root.bind1stage("max_members", UniValueNode.ValueType.INTEGER);
        VarBind.TwoStage<String, URL> VanityUrl
                = Root.bind2stage("vanity_url", UniValueNode.ValueType.STRING, code -> Polyfill.url("https://discord.gg/" + code));
        VarBind.OneStage<String> Description
                = Root.bind1stage("description", UniValueNode.ValueType.STRING);
        VarBind.OneStage<String> BannerHash // todo
                = Root.bind1stage("banner", UniValueNode.ValueType.STRING);
        VarBind.TwoStage<Integer, PremiumTierLevel> BoostLevel
                = Root.bind2stage("premium_tier", UniValueNode.ValueType.INTEGER, PremiumTierLevel::valueOf);
        VarBind.OneStage<Integer> BoostCount
                = Root.bind1stage("premium_subscription_count", UniValueNode.ValueType.INTEGER);
        VarBind.TwoStage<String, Locale> Locale
                = Root.bind2stage("preferred_locale", UniValueNode.ValueType.STRING, java.util.Locale::new);
        VarBind.DependentTwoStage<Long, DiscordBot, ? extends GuildChannel> PublicUpdatesChannel
                = Root.bindDependent("public_updates_channel_id", UniValueNode.ValueType.LONG, (bot, id) -> bot.getGuildChannelByID(id).get());
        VarBind.OneStage<Integer> ApproximateMemberCount
                = Root.bind1stage("approximate_member_count", UniValueNode.ValueType.INTEGER);
        VarBind.OneStage<Integer> ApproximatePresenceCount
                = Root.bind1stage("approximate_presence_count", UniValueNode.ValueType.INTEGER);
    }

    final class Basic extends Snowflake.Base implements Guild {
        protected Basic(@Nullable UniObjectNode initialData, @NotNull DiscordBot dependencyObject) {
            super(initialData, dependencyObject);
        }
    }
}
