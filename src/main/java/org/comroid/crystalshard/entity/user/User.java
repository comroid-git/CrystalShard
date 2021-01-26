package org.comroid.crystalshard.entity.user;

import org.comroid.crystalshard.cdn.ImageType;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.entity.channel.PrivateTextChannel;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.crystalshard.entity.message.Message;
import org.comroid.crystalshard.model.message.MessageBuilder;
import org.comroid.crystalshard.model.message.MessageTarget;
import org.comroid.crystalshard.model.user.PremiumType;
import org.comroid.mutatio.ref.Reference;

import java.net.URL;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface User extends Snowflake, MessageTarget {
    String getUsername();

    default String getDisplayName(Guild inGuild) {
        return asGuildMember(inGuild)
                .flatMap(member -> member.nickname)
                .orElseGet(this::getUsername);
    }

    String getDiscriminator();

    boolean isBot();

    boolean isSystemUser();

    boolean isMfaEnabled();

    Locale getPreferredLocale();

    boolean isVerified();

    String getEMail();

    PremiumType getPremiumType();

    Set<SimpleUser.Flags> getAllFlags();

    Set<SimpleUser.Flags> getPublicFlags();

    URL getAvatarURL(ImageType imageType);

    Reference<GuildMember> asGuildMember(Guild guild);

    CompletableFuture<GuildMember> requestGuildMember(Guild guild);

    CompletableFuture<PrivateTextChannel> openPrivateChannel();

    @Override
    default CompletableFuture<Message> executeMessage(MessageBuilder builder) {
        return openPrivateChannel().thenCompose(ptc -> ptc.executeMessage(builder));
    }
}
