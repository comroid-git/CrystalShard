package de.kaleidox.crystalshard.internal.items.server;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.handling.editevent.EditTrait;
import de.kaleidox.crystalshard.main.items.channel.VoiceChannel;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.server.VoiceState;
import de.kaleidox.crystalshard.main.items.user.User;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static de.kaleidox.crystalshard.main.handling.editevent.enums.VoiceStateEditTrait.*;

/**
 * guild_id?	snowflake	    the guild id this voice state is for channel_id	?snowflake	    the channel id this user is connected to user_id      snowflake
 * the user id this voice state is for member?      member object	the guild member this voice state is for session_id	string	        the session id for this
 * voice state deaf	bool whether this user is deafened by the server mute	bool	whether         this user is muted by the server self_deaf bool whether
 * this user
 * is locally deafened self_mute	bool	        whether this user is locally muted suppress bool whether this user is muted by the current user
 */
public class VoiceStateInternal implements VoiceState {
    private final static ConcurrentHashMap<String, VoiceStateInternal> instances = new ConcurrentHashMap<>();
    private final Discord discord;
    private final Server server;
    private final User user;
    private final String sessionId;
    private VoiceChannel channel;
    private boolean deafened;
    private boolean muted;
    private boolean selfMuted;
    private boolean selfDeafened;
    private boolean suppressed;

    private VoiceStateInternal(Discord discord, JsonNode data) {
        this.discord = discord;
        long serverId = data.get("guild_id")
                .asLong(-1);
        this.server = serverId == -1 ? null : discord.getServerCache()
                .getOrRequest(serverId, serverId);
        long userId = data.get("user_id")
                .asLong();
        user = discord.getUserCache()
                .getOrRequest(userId, userId);
        this.sessionId = data.get("session_id")
                .asText();
        updateData(data);

        instances.entrySet()
                .stream()
                .filter(entry -> entry.getValue()
                        .getUser()
                        .equals(user))
                .forEach(entry -> instances.remove(entry.getKey(),
                        entry.getValue()));
        instances.put(sessionId, this);
    }

    // Static members
    // Static membe
    public static VoiceState getInstance(Discord discord, JsonNode data) {
        return instances.getOrDefault(data.get("session_id")
                .asText(), new VoiceStateInternal(discord, data));
    }

    // Override Methods
    @Override
    public Discord getDiscord() {
        return null;
    }

    @Override
    public Optional<Server> getServer() {
        return Optional.empty();
    }

    @Override
    public VoiceChannel getChannel() {
        return null;
    }

    @Override
    public User getUser() {
        return null;
    }

    @Override
    public boolean isDeafened() {
        return false;
    }

    @Override
    public boolean isMuted() {
        return false;
    }

    @Override
    public boolean isSelfDeafened() {
        return false;
    }

    @Override
    public boolean isSelfMuted() {
        return false;
    }

    @Override
    public boolean isSuppressed() {
        return false;
    }

    public Set<EditTrait<VoiceState>> updateData(JsonNode data) {
        Set<EditTrait<VoiceState>> traits = new HashSet<>();

        long channelId = data.path("channel_id")
                .asLong(channel.getId());
        VoiceChannel newChannel = discord.getChannelCache()
                .getOrRequest(channelId, channelId)
                .toVoiceChannel()
                .orElseThrow(AssertionError::new);
        if (!channel.equals(newChannel)) {
            this.channel = newChannel;
            traits.add(CHANNEL);
        }
        if (deafened != data.path("deaf")
                .asBoolean(deafened)) {
            this.deafened = data.get("deaf")
                    .asBoolean();
            traits.add(DEAFENED_STATE);
        }
        if (muted != data.path("mute")
                .asBoolean(muted)) {
            this.muted = data.get("mute")
                    .asBoolean();
            traits.add(MUTED_STATE);
        }
        if (selfDeafened != data.path("self_deaf")
                .asBoolean(selfDeafened)) {
            this.selfDeafened = data.get("self_deaf")
                    .asBoolean();
            traits.add(SELF_DEAFENED_STATE);
        }
        if (selfMuted != data.get("self_mute")
                .asBoolean(selfMuted)) {
            this.selfMuted = data.get("self_mute")
                    .asBoolean();
            traits.add(SELF_MUTED_STATE);
        }
        if (suppressed != data.get("suppress")
                .asBoolean(suppressed)) {
            this.suppressed = data.get("suppress")
                    .asBoolean();
            traits.add(SUPPRESSED_STATE);
        }

        return traits;
    }
}
