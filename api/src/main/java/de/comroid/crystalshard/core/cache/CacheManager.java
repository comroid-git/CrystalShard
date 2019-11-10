package de.comroid.crystalshard.core.cache;

import java.util.Optional;
import java.util.stream.Stream;

import de.comroid.crystalshard.api.entity.Snowflake;
import de.comroid.crystalshard.api.entity.channel.Channel;
import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.api.entity.guild.Role;
import de.comroid.crystalshard.api.entity.message.Message;
import de.comroid.crystalshard.api.entity.user.GuildMember;
import de.comroid.crystalshard.api.entity.user.User;
import de.comroid.crystalshard.api.model.ApiBound;
import de.comroid.crystalshard.api.model.guild.ban.Ban;

public interface CacheManager extends ApiBound {
    <R extends Cacheable> Optional<R> set(Class<R> type, long id, R instance);

    <M extends Cacheable, B extends Cacheable> Optional<M> setMember(
            Class<B> baseType,
            Class<M> memberType,
            long baseId,
            long memberId,
            M instance
    );

    <M extends Cacheable, B extends Cacheable> Optional<M> setSingleton(
            Class<B> baseType,
            Class<M> memberType,
            long baseId,
            M instance
    );

    <R extends Cacheable> Void delete(Class<R> type, long id);

    <B extends Cacheable, M extends Cacheable> Void deleteMember(
            Class<B> baseType,
            Class<M> memberType,
            long baseId,
            long memberId
    );

    <R extends Cacheable> Cache<R> getCache(Class<R> forType);

    default Optional<Guild> getGuildByID(long id) {
        return getCache(Guild.class).getByID(id);
    }

    default Optional<Channel> getChannelByID(long id) {
        return getCache(Channel.class).getByID(id);
    }

    default Optional<User> getUserByID(long id) {
        return getCache(User.class).getByID(id);
    }

    default Optional<GuildMember> getGuildMemberByID(long guildId, long id) {
        return getCache(Guild.class)
                .getMemberCache(guildId, GuildMember.class)
                .getByID(id);
    }
    
    default Optional<Role> getRoleByID(long id) {
        return getByID(Role.class, id);
    }

    default Optional<Role> getRoleByID(long guildId, long id) {
        return getCache(Guild.class)
                .getMemberCache(guildId, Role.class)
                .getByID(id);
    }
    
    default Optional<Message> getMessageByID(long id) {
        return getByID(Message.class, id);
    }

    default Optional<Message> getMessageByID(long channelId, long id) {
        return getCache(Channel.class)
                .getMemberCache(channelId, Message.class)
                .getByID(id);
    }

    default Optional<Ban> getBanByUserID(long guildId, long userId) {
        return getCache(Guild.class)
                .getMemberCache(guildId, Ban.class)
                .getByID(userId);
    }

    default <T extends Snowflake> Stream<T> streamSnowflakesByID(Class<T> type, long id) {
        return streamSnowflakesByID(id)
                .filter(type::isInstance)
                .map(type::cast);
    }

    Stream<Snowflake> streamSnowflakesByID(long id);

    default <T extends Snowflake> Optional<T> getByID(Class<T> type, long id) {
        return streamSnowflakesByID(id)
                .filter(type::isInstance)
                .findFirst()
                .map(type::cast);
    }
}
