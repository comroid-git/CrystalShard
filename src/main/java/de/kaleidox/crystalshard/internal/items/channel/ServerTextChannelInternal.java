package de.kaleidox.crystalshard.internal.items.channel;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.items.permission.PermissionOverrideInternal;
import de.kaleidox.crystalshard.internal.items.server.ServerInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.handling.editevent.EditTrait;
import de.kaleidox.crystalshard.main.items.channel.Channel;
import de.kaleidox.crystalshard.main.items.channel.ChannelCategory;
import de.kaleidox.crystalshard.main.items.channel.ServerTextChannel;
import de.kaleidox.crystalshard.main.items.permission.PermissionOverride;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.util.helpers.ListHelper;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static de.kaleidox.crystalshard.main.handling.editevent.enums.ChannelEditTrait.*;

public class ServerTextChannelInternal extends TextChannelInternal implements ServerTextChannel {
    final static ConcurrentHashMap<Long, ServerTextChannel> instances = new ConcurrentHashMap<>();
    boolean isNsfw;
    String topic;
    String name;
    final List<PermissionOverride> overrides;
    final Server server;
    ChannelCategory category;

    private ServerTextChannelInternal(Discord discord, Server server, JsonNode data) {
        super(discord, data);
        this.server = server;
        this.name = data.path("name").asText("");
        this.isNsfw = data.path("nsfw").asBoolean(false);
        this.topic = data.path("topic").asText(null);
        this.category = ChannelInternal.getInstance(discord, data.path("parent_id").asLong(0))
                .toChannelCategory()
                .orElse(null);

        this.overrides = new ArrayList<>();
        data.path("permission_overwrites")
                .forEach(node -> overrides.add(new PermissionOverrideInternal(discord, server, node)));

        instances.put(id, this);
    }

    @Override
    public Set<EditTrait<Channel>> updateData(JsonNode data) {
        Set<EditTrait<Channel>> traits = new HashSet<>();

        if (isNsfw != data.path("nsfw").asBoolean(isNsfw)) {
            isNsfw = data.get("nsfw").asBoolean();
            traits.add(NSFW_FLAG);
        }
        if (!topic.equals(data.path("topic").asText(topic))) {
            topic = data.get("topic").asText();
            traits.add(TOPIC);
        }
        if (!name.equals(data.path("name").asText(name))) {
            name = data.get("name").asText();
            traits.add(NAME);
        }
        if (this.category == null && data.has("parent_id")) {
            ChannelCategory category = ChannelInternal.getInstance(discord, data.path("parent_id").asLong(
                    this.category == null ? 0 : this.category.getId()))
                    .toChannelCategory()
                    .orElse(null);
            if (!this.category.equals(category)) {
                this.category = category;
                traits.add(CATEGORY);
            }
        }
        List<PermissionOverride> overrides = new ArrayList<>();
        data.path("permission_overwrites")
                .forEach(node -> overrides.add(new PermissionOverrideInternal(discord, server, node)));
        if (!ListHelper.equalContents(overrides, this.overrides)) {
            this.overrides.clear();
            this.overrides.addAll(overrides);
            traits.add(PERMISSION_OVERWRITES);
        }

        return traits;
    }

    public static ServerTextChannel getInstance(Discord discord, Server server, JsonNode data) {
        long id = data.get("id").asLong(-1);
        if (id == -1) throw new NoSuchElementException("No valid ID found.");
        if (server == null) server = ServerInternal.getInstance(discord, data.path("guild_id").asLong(0));
        if (instances.containsKey(id))
            return instances.get(id);
        else
            return new ServerTextChannelInternal(discord, server, data);
    }

    @Override
    public Server getServer() {
        return server;
    }

    @Override
    public Optional<ChannelCategory> getCategory() {
        return Optional.ofNullable(category);
    }

    @Override
    public List<PermissionOverride> getPermissionOverrides() {
        return overrides;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getTopic() {
        return topic;
    }
}
