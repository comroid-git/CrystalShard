package de.kaleidox.crystalshard.internal.items.channel;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.items.permission.PermissionOverrideInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.handling.editevent.EditTrait;
import de.kaleidox.crystalshard.main.handling.editevent.enums.ChannelEditTrait;
import de.kaleidox.crystalshard.main.items.channel.Channel;
import de.kaleidox.crystalshard.main.items.channel.ChannelCategory;
import de.kaleidox.crystalshard.main.items.permission.PermissionOverride;
import de.kaleidox.crystalshard.main.items.server.Server;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ChannelCategoryInternal extends ChannelInternal implements ChannelCategory {
    final static ConcurrentHashMap<Long, ChannelCategory> instances = new ConcurrentHashMap<>();
    String name;
    final Server server;
    private final List<PermissionOverride> overrides;

    private ChannelCategoryInternal(Discord discord, Server server, JsonNode data) {
        super(discord, data);
        this.server = server;
        this.name = data.path("name").asText("");

        this.overrides = new ArrayList<>();
        data.path("permission_overwrites")
                .forEach(node -> overrides.add(new PermissionOverrideInternal(discord, server, node)));

        instances.put(id, this);
    }

    @Override
    public Set<EditTrait<Channel>> updateData(JsonNode data) {
        Set<EditTrait<Channel>> traits = new HashSet<>();

        if (!name.equals(data.path("name").asText(name))) {
            name = data.get("name").asText();
            traits.add(ChannelEditTrait.NAME);
        }

        return traits;
    }

    public static ChannelCategory getInstance(Discord discord, Server server, JsonNode data) {
        long id = data.get("id").asLong(-1);
        if (id == -1) throw new NoSuchElementException("No valid ID found.");
        if (instances.containsKey(id))
            return instances.get(id);
        else
            return new ChannelCategoryInternal(discord, server, data);
    }

    @Override
    public Server getServer() {
        return server;
    }

    @Override
    public Optional<ChannelCategory> getCategory() {
        return Optional.of(this);
    }

    @Override
    public List<PermissionOverride> getPermissionOverrides() {
        return overrides;
    }

    @Override
    public String getName() {
        return name;
    }
}
