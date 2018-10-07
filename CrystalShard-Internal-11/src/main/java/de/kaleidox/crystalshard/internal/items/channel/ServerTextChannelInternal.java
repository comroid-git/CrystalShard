package de.kaleidox.crystalshard.internal.items.channel;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.core.net.request.Endpoint;
import de.kaleidox.crystalshard.core.net.request.Method;
import de.kaleidox.crystalshard.core.net.request.WebRequest;
import de.kaleidox.crystalshard.internal.items.permission.PermissionOverrideInternal;
import de.kaleidox.crystalshard.internal.items.server.interactive.InviteInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.exception.DiscordPermissionException;
import de.kaleidox.crystalshard.main.handling.editevent.EditTrait;
import de.kaleidox.crystalshard.main.items.channel.Channel;
import de.kaleidox.crystalshard.main.items.channel.ChannelCategory;
import de.kaleidox.crystalshard.main.items.channel.ServerChannel;
import de.kaleidox.crystalshard.main.items.channel.ServerTextChannel;
import de.kaleidox.crystalshard.main.items.permission.Permission;
import de.kaleidox.crystalshard.main.items.permission.PermissionOverride;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.server.interactive.MetaInvite;
import de.kaleidox.crystalshard.main.items.user.User;
import de.kaleidox.crystalshard.util.helpers.ListHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
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
    
    public ServerTextChannelInternal(Discord discord, Server server, JsonNode data) {
        super(discord, data);
        this.server = server;
        this.overrides = new ArrayList<>();
        updateData(data);
        
        data.path("permission_overwrites").forEach(node -> overrides.add(new PermissionOverrideInternal(discord, server, node)));
        
        instances.put(id, this);
    }
    
    // Override Methods
    @Override
    public Set<EditTrait<Channel>> updateData(JsonNode data) {
        Set<EditTrait<Channel>> traits = new HashSet<>();
        
        if (isNsfw != data.path("nsfw").asBoolean(isNsfw)) {
            isNsfw = data.get("nsfw").asBoolean();
            traits.add(NSFW_FLAG);
        }
        if (topic == null || !topic.equals(data.path("topic").asText(topic))) {
            topic = data.get("topic").asText();
            traits.add(TOPIC);
        }
        if (name == null || !name.equals(data.path("name").asText(name))) {
            name = data.get("name").asText();
            traits.add(NAME);
        }
        //noinspection ConstantConditions
        if (category == null || (this.category == null && data.has("parent_id"))) {
            long parentId = data.path("parent_id").asLong(-1);
            this.category = parentId == -1 ? null : discord.getChannelCache().getOrRequest(parentId, parentId).toChannelCategory().orElse(null);
        } else //noinspection ConstantConditions
            if (this.category != null && !data.has("parent_id")) {
                this.category = null;
            }
        List<PermissionOverride> overrides = new ArrayList<>();
        data.path("permission_overwrites").forEach(node -> overrides.add(new PermissionOverrideInternal(discord, server, node)));
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
    public CompletableFuture<Collection<MetaInvite>> getChannelInvites() {
        if (!hasPermission(discord, Permission.MANAGE_CHANNELS)) return CompletableFuture.failedFuture(new DiscordPermissionException(
                "Cannot get channel invite!",
                Permission.MANAGE_CHANNELS));
        return new WebRequest<Collection<MetaInvite>>(discord).method(Method.GET).endpoint(Endpoint.Location.CHANNEL_INVITE.toEndpoint(id)).execute(data -> {
            List<MetaInvite> list = new ArrayList<>();
            data.forEach(invite -> list.add(new InviteInternal.Meta(discord, invite)));
            return list;
        });
    }
    
    @Override
    public InviteBuilder getInviteBuilder() {
        return new ChannelBuilderInternal.ChannelInviteBuilder(this);
    }
    
    @Override
    public boolean isNsfw() {
        return isNsfw;
    }
    
    @Override
    public ServerTextChannel.Updater getUpdater() {
        return new ChannelUpdaterInternal.ServerTextChannelUpdater(discord, this);
    }
    
    @Override
    public boolean hasPermission(User user, Permission permission) {
        return overrides.stream()
                .filter(override -> override.getParent() != null)
                .filter(override -> override.getParent().equals(user))
                .map(override -> override.getAllowed().contains(permission))
                .findAny()
                .or(() -> this.getCategory().flatMap(channelCategory -> channelCategory.getPermissionOverrides()
                        .stream()
                        .filter(override -> override.getParent() != null)
                        .filter(override -> override.getParent().equals(user))
                        .findAny()).map(override -> override.getAllowed().contains(permission)))
                .or(() -> Optional.of(toServerChannel().map(ServerChannel::getServer)
                                              .orElseThrow(AssertionError::new)
                                              .getEveryoneRole()
                                              .getPermissions()
                                              .contains(Permission.SEND_MESSAGES)))
                .orElse(true); // if no information could be found, assert TRUE
    }
}
