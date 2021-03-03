package org.comroid.crystalshard.entity.user;

import org.comroid.api.Rewrapper;
import org.comroid.crystalshard.cdn.ImageType;
import org.comroid.crystalshard.entity.EntityType;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.entity.channel.PrivateTextChannel;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.crystalshard.entity.guild.Role;
import org.comroid.crystalshard.model.AbstractDataContainer;
import org.comroid.crystalshard.model.DiscordDataContainer;
import org.comroid.crystalshard.model.user.PremiumType;
import org.comroid.mutatio.ref.Reference;
import org.comroid.mutatio.span.Span;
import org.comroid.uniform.node.UniNode;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.util.StandardValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.Nullable;

import java.net.URL;
import java.time.Instant;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public final class GuildMember extends AbstractDataContainer implements User {
    @RootBind
    public static final GroupBind<GuildMember> TYPE
            = AbstractDataContainer.BASETYPE.subGroup("guild-member");
    public static final VarBind<GuildMember, UniObjectNode, User, User> USER
            = TYPE.createBind("user")
            .extractAsObject()
            .andResolve(SimpleUser::resolve)
            .build();
    public static final VarBind<GuildMember, String, String, String> NICKNAME
            = TYPE.createBind("nick")
            .extractAs(StandardValueType.STRING)
            .build();
    public static final VarBind<GuildMember, Long, Role, Span<Role>> ROLES
            = TYPE.createBind("roles")
            .extractAsArray(StandardValueType.LONG)
            .andResolveRef((gmb, id) -> gmb.getCache().getRole(id))
            .intoSpan()
            .build();
    public static final VarBind<GuildMember, String, String, String> JOINED_AT
            = TYPE.createBind("joined_at")
            .extractAs(StandardValueType.STRING)
            .build();
    public static final VarBind<GuildMember, String, Instant, Instant> PREMIUM_SINCE
            = TYPE.createBind("premium_since")
            .extractAs(StandardValueType.STRING)
            .andRemap(Instant::parse)
            .build();
    public static final VarBind<GuildMember, Boolean, Boolean, Boolean> IS_DEAFENED
            = TYPE.createBind("deaf")
            .extractAs(StandardValueType.BOOLEAN)
            .build();
    public static final VarBind<GuildMember, Boolean, Boolean, Boolean> IS_MUTED
            = TYPE.createBind("mute")
            .extractAs(StandardValueType.BOOLEAN)
            .build();
    public static final VarBind<GuildMember, Boolean, Boolean, Boolean> IS_PENDING
            = TYPE.createBind("pending")
            .extractAs(StandardValueType.BOOLEAN)
            .build();
    private final User base;
    public final Reference<String> nickname = getComputedReference(NICKNAME);
    public final Reference<String> joinedAt = getComputedReference(JOINED_AT);
    public final Reference<Instant> premiumSince = getComputedReference(PREMIUM_SINCE);
    public final Reference<Boolean> isDeafened = getComputedReference(IS_DEAFENED);
    public final Reference<Boolean> isMuted = getComputedReference(IS_MUTED);
    public final Reference<Boolean> isPending = getComputedReference(IS_PENDING);

    public String getNickname() {
        return nickname.assertion();
    }

    public Span<Role> getRoles() {
        return getComputedReference(ROLES).orElseGet(Span::empty);
    }

    public String getJoinedAt() {
        return joinedAt.assertion();
    }

    public Instant getPremiumSince() {
        return premiumSince.assertion();
    }

    public boolean isDeafened() {
        return isDeafened.assertion();
    }

    public boolean isMuted() {
        return isMuted.assertion();
    }

    public boolean isPending() {
        return isPending.assertion();
    }

    @Override
    public long getID() {
        return base.getID();
    }

    @Override
    public EntityType<? extends Snowflake> getEntityType() {
        return base.getEntityType();
    }

    @Override
    public String getUsername() {
        return base.getUsername();
    }

    @Override
    public String getDiscriminator() {
        return base.getDiscriminator();
    }

    @Override
    public boolean isBot() {
        return base.isBot();
    }

    @Override
    public boolean isSystemUser() {
        return base.isSystemUser();
    }

    @Override
    public boolean isMfaEnabled() {
        return base.isMfaEnabled();
    }

    @Override
    public Locale getPreferredLocale() {
        return base.getPreferredLocale();
    }

    @Override
    public boolean isVerified() {
        return base.isVerified();
    }

    @Override
    public String getEMail() {
        return base.getEMail();
    }

    @Override
    public PremiumType getPremiumType() {
        return base.getPremiumType();
    }

    @Override
    public Set<SimpleUser.Flags> getAllFlags() {
        return base.getAllFlags();
    }

    @Override
    public Set<SimpleUser.Flags> getPublicFlags() {
        return base.getPublicFlags();
    }

    @Override
    public URL getAvatarURL(ImageType imageType) {
        return base.getAvatarURL(imageType);
    }

    @Override
    public Reference<GuildMember> asGuildMember(Guild guild) {
        return Reference.constant(this);
    }

    @Override
    public CompletableFuture<GuildMember> requestGuildMember(Guild guild) {
        return CompletableFuture.completedFuture(this);
    }

    @Override
    public CompletableFuture<PrivateTextChannel> openPrivateChannel() {
        return base.openPrivateChannel();
    }

    public static GuildMember resolve(Guild context, UniNode data) {
        if (!data.has(USER))
            return null;
        User user = SimpleUser.resolve(context, data.get(USER));
        return user.as(SimpleUser.class, "assertion").createGuildInstance(context, data.asObjectNode());
    }

    GuildMember(User baseUser, @Nullable UniNode initialData) {
        super(baseUser, initialData);

        this.base = baseUser;
    }
}
