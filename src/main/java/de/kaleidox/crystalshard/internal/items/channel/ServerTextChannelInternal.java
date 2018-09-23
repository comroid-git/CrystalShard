package de.kaleidox.crystalshard.internal.items.channel;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.items.permission.PermissionOverrideInternal;
import de.kaleidox.crystalshard.internal.items.server.ServerInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.handling.editevent.EditTrait;
import de.kaleidox.crystalshard.main.items.channel.Channel;
import de.kaleidox.crystalshard.main.items.channel.ChannelCategory;
import de.kaleidox.crystalshard.main.items.channel.ServerChannel;
import de.kaleidox.crystalshard.main.items.channel.ServerTextChannel;
import de.kaleidox.crystalshard.main.items.permission.Permission;
import de.kaleidox.crystalshard.main.items.permission.PermissionOverride;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.user.User;
import de.kaleidox.util.helpers.ListHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static de.kaleidox.crystalshard.main.handling.editevent.enums.ChannelEditTrait.*;

public class ServerTextChannelInternal extends TextChannelInternal implements ServerTextChannel {
    final static ConcurrentHashMap<Long, ServerTextChannel> instances = new ConcurrentHashMap<>();
    final        List<PermissionOverride>                   overrides;
    final        Server                                     server;
    boolean         isNsfw;
    String          topic;
    String          name;
    ChannelCategory category;
    
    private ServerTextChannelInternal(Discord discord, Server server, JsonNode data) {
        super(discord, data);
        this.server = server;
        this.name = data.path("name")
                .asText("");
        this.isNsfw = data.path("nsfw")
                .asBoolean(false);
        this.topic = data.path("topic")
                .asText(null);
        this.category = data.has("parent_id") && !data.path("parent_id")
                .isNull() ? ChannelInternal.getInstance(discord,
                                                        data.path("parent_id")
                                                                .asLong())
                                .toChannelCategory()
                                .orElse(null) : null;
        
        this.overrides = new ArrayList<>();
        data.path("permission_overwrites")
                .forEach(node -> overrides.add(new PermissionOverrideInternal(discord, server, node)));
        
        instances.put(id, this);
    }
    
    // Override Methods
    @Override
    public Set<EditTrait<Channel>> updateData(JsonNode data) {
        Set<EditTrait<Channel>> traits = new HashSet<>();
        
        if (isNsfw != data.path("nsfw")
                .asBoolean(isNsfw)) {
            isNsfw = data.get("nsfw")
                    .asBoolean();
            traits.add(NSFW_FLAG);
        }
        if (!topic.equals(data.path("topic")
                                  .asText(topic))) {
            topic = data.get("topic")
                    .asText();
            traits.add(TOPIC);
        }
        if (!name.equals(data.path("name")
                                 .asText(name))) {
            name = data.get("name")
                    .asText();
            traits.add(NAME);
        }
        if (this.category == null && data.has("parent_id")) {
            ChannelCategory category = ChannelInternal.getInstance(discord,
                                                                   data.path("parent_id")
                                                                           .asLong(this.category == null ? 0 :
                                                                                   this.category.getId()))
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
    public int getPosition() {
        return 0; // todo
    }
    
    @Override
    public String getTopic() {
        return topic;
    }
    
    @Override
    public boolean isNsfw() {
        return isNsfw;
    }
    
    @Override
    public Channel.Updater getUpdater() {
        return new ChannelUpdaterInternal.TextChannelUpdater(this);
    }
    
    @Override
    public boolean hasPermission(User user, Permission permission) {
        return overrides.stream()
                .filter(override -> override.getParent() != null)
                .filter(override -> override.getParent()
                        .equals(user))
                .map(override -> override.getAllowed()
                        .contains(permission))
                .findAny()
                .or(() -> this.getCategory()
                        .flatMap(channelCategory -> channelCategory.getPermissionOverrides()
                                .stream()
                                .filter(override -> override.getParent() != null)
                                .filter(override -> override.getParent()
                                        .equals(user))
                                .findAny())
                        .map(override -> override.getAllowed()
                                .contains(permission)))
                .or(() -> Optional.of(toServerChannel().map(ServerChannel::getServer)
                                              .orElseThrow(AssertionError::new)
                                              .getEveryoneRole()
                                              .getPermissions()
                                              .contains(Permission.SEND_MESSAGES)))
                .orElse(true); // if no information could be found, assert TRUE
    }
    
    // Static members
    public static ServerTextChannel getInstance(Discord discord, Server server, JsonNode data) {
        long id = data.get("id")
                .asLong(-1);
        if (id == -1) throw new NoSuchElementException("No valid ID found.");
        if (server == null) server = ServerInternal.getInstance(discord,
                                                                data.path("guild_id")
                                                                        .asLong(0));
        if (instances.containsKey(id)) return instances.get(id);
        else return new ServerTextChannelInternal(discord, server, data);
    }
}
