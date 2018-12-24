package de.kaleidox.crystalshard.internal.items.channel;

import com.fasterxml.jackson.databind.JsonNode;

import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.entity.channel.Channel;
import de.kaleidox.crystalshard.api.entity.channel.GroupChannel;
import de.kaleidox.crystalshard.api.entity.permission.Permission;
import de.kaleidox.crystalshard.api.entity.user.User;
import de.kaleidox.crystalshard.api.handling.editevent.EditTrait;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class GroupChannelInternal extends TextChannelInternal implements GroupChannel {
    final static ConcurrentHashMap<Long, GroupChannel> instances = new ConcurrentHashMap<>();

    public GroupChannelInternal(Discord discord, JsonNode data) {
        super(discord, data);

        instances.put(id, this);
    }

    // Override Methods
    @Override
    public boolean hasPermission(User user, Permission permission) {
        return true;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public int getPosition() {
        return 0;
    }

    @Override
    public Set<EditTrait<Channel>> updateData(JsonNode data) {
        Set<EditTrait<Channel>> traits = new HashSet<>();

        return traits;
    }
}
