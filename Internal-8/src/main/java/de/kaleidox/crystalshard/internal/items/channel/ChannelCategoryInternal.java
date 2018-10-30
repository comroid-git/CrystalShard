package de.kaleidox.crystalshard.internal.items.channel;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.items.permission.PermissionOverrideInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.handling.editevent.EditTrait;
import de.kaleidox.crystalshard.main.handling.editevent.enums.ChannelEditTrait;
import de.kaleidox.crystalshard.main.items.channel.Channel;
import de.kaleidox.crystalshard.main.items.channel.ChannelCategory;
import de.kaleidox.crystalshard.main.items.channel.ServerChannel;
import de.kaleidox.crystalshard.main.items.permission.Permission;
import de.kaleidox.crystalshard.main.items.permission.PermissionOverride;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.server.interactive.MetaInvite;
import de.kaleidox.crystalshard.main.items.user.User;

import de.kaleidox.crystalshard.util.helpers.OptionalHelper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class ChannelCategoryInternal extends ChannelInternal implements ChannelCategory {
    final static  ConcurrentHashMap<Long, ChannelCategory> instances = new ConcurrentHashMap<>();
    final         Server                                   server;
    private final List<PermissionOverride>                 overrides;
    String name;
    
    public ChannelCategoryInternal(Discord discord, Server server, JsonNode data) {
        super(discord, data);
        this.server = server;
        this.name = data.path("name").asText("");
        
        this.overrides = new ArrayList<>();
        data.path("permission_overwrites").forEach(node -> overrides.add(new PermissionOverrideInternal(discord, server, node)));
        
        instances.put(id, this);
    }
    
    // Override Methods
    @Override
    public Set<EditTrait<Channel>> updateData(JsonNode data) {
        Set<EditTrait<Channel>> traits = new HashSet<>();
        
        if (!name.equals(data.path("name").asText(name))) {
            name = data.get("name").asText();
            traits.add(ChannelEditTrait.NAME);
        }
        
        return traits;
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
    public CompletableFuture<Collection<MetaInvite>> getChannelInvites() {
        return null;
    }
    
    @Override
    public InviteBuilder getInviteBuilder() {
        return new ChannelBuilderInternal.ChannelInviteBuilder(this);
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public int getPosition() {
        return 0;
    }
    
    @Override
    public boolean hasPermission(User user, Permission permission) {
        return OptionalHelper.or(overrides.stream()
                .filter(override -> override.getParent() != null)
                .filter(override -> override.getParent().equals(user))
                .map(override -> override.getAllowed().contains(permission))
                .findAny(), () -> Optional.of(server.getEveryoneRole().getPermissions().contains(permission)))
                .orElse(true); // If no information could be acquired, assert TRUE
    }
    
    @Override
    public ServerChannel.Updater getUpdater() {
        return new ChannelUpdaterInternal.ServerCategoryUpdater(discord, this);
    }
}
