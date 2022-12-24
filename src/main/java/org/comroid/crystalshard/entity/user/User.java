package org.comroid.crystalshard.entity.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import org.comroid.crystalshard.cdn.ImageType;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.entity.channel.PrivateTextChannel;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.crystalshard.entity.message.Message;
import org.comroid.crystalshard.model.message.MessageBuilder;
import org.comroid.crystalshard.model.message.MessageTarget;
import org.comroid.crystalshard.model.user.PremiumType;

import java.net.URL;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Entity
public class User extends Snowflake implements MessageTarget {
    @Column
    @Getter
    private String username;
    @Column
    @Getter
    private String discriminator;
    @Column
    @Getter
    private boolean bot;
    @Column
    @Getter
    private boolean systemUser;
    @Column
    @Getter
    private boolean mfaEnabled;
    @Column
    @Getter
    private Locale preferredLocale;
    @Column
    @Getter
    private boolean verified;
    @Column
    @Getter
    private String email;
    @Column
    @Getter
    private PremiumType premiumType;

    Set<SimpleUser.Flags> getAllFlags();

    Set<SimpleUser.Flags> getPublicFlags();

    default String getDisplayName(Guild inGuild) {
        return asGuildMember(inGuild)
                .flatMap(member -> member.nickname)
                .orElseGet(this::getUsername);
    }

    URL getAvatarURL(ImageType imageType);

    Optional<GuildMember> asGuildMember(Guild guild);

    CompletableFuture<GuildMember> requestGuildMember(Guild guild);

    CompletableFuture<PrivateTextChannel> openPrivateChannel();

    @Override
    default CompletableFuture<Message> executeMessage(MessageBuilder builder) {
        return openPrivateChannel().thenCompose(ptc -> ptc.executeMessage(builder));
    }
}
