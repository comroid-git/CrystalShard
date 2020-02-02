package org.comroid.crystalshard.impl.model.user;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.comroid.common.spellbind.Spellbind;
import org.comroid.crystalshard.CrystalShard;
import org.comroid.crystalshard.abstraction.entity.AbstractSnowflake;
import org.comroid.crystalshard.api.Discord;
import org.comroid.crystalshard.api.entity.channel.PrivateTextChannel;
import org.comroid.crystalshard.api.entity.guild.Guild;
import org.comroid.crystalshard.api.entity.message.Message;
import org.comroid.crystalshard.api.entity.user.GuildMember;
import org.comroid.crystalshard.api.entity.user.User;
import org.comroid.crystalshard.api.event.multipart.user.UserEvent;
import org.comroid.crystalshard.api.model.Partial;
import org.comroid.crystalshard.api.model.channel.MessagePrintStream;
import org.comroid.crystalshard.util.model.NonThrowingCloseable;

import com.alibaba.fastjson.JSONObject;

public class UserImpl extends AbstractSnowflake<UserImpl> implements User {
    private final Map<Long, GuildMember> guildMemberProxies = new ConcurrentHashMap<>();

    @Override
    public Optional<GuildMember> asGuildMember(Guild guild) {
        return Optional.ofNullable(guildMemberProxies.computeIfAbsent(guild.getID(), guildId -> Spellbind.builder(GuildMember.class)
                .classloader(CrystalShard.class.getClassLoader())
                .coreObject(this)
                .subImplement(new PartialGuildMember(this, guild), Partial.GuildMember.class)
                .build()));
    }

    public UserImpl(Discord api, JSONObject data) {
        super(api, data);
    }

    @Override public CompletableFuture<PrivateTextChannel> openPrivateMessageChannel() {
        return null;
    }

    @Override public <X extends UserEvent> API<X> listenTo(Class<X> eventType) {
        return null;
    }

    @Override public <X extends UserEvent> NonThrowingCloseable listenUsing(EventAdapter<X> eventAdapter) {
        return null;
    }

    @Override public boolean detachHandlerIf(Class<? extends UserEvent> targetType, Predicate<Consumer<UserEvent>> filter) {
        return false;
    }

    @Override public boolean detachAdapterIf(Class<? extends UserEvent> targetType, Predicate<EventAdapter<UserEvent>> filter) {
        return false;
    }

    @Override public void submitEvent(UserEvent event) {

    }

    @Override public String getMentionTag() {
        return null;
    }

    @Override public Optional<Message> getLatestMessage() {
        return Optional.empty();
    }

    @Override public Collection<Message> getMessages(int limit) {
        return null;
    }

    @Override public Stream<Message> getLatestMessagesAsStream() {
        return null;
    }

    @Override public Message.Composer composeMessage() {
        return null;
    }

    @Override public MessagePrintStream openPrintStream() {
        return null;
    }
}
