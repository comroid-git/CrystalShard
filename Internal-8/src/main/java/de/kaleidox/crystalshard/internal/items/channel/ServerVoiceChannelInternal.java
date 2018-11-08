package de.kaleidox.crystalshard.internal.items.channel;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.core.CoreDelegate;
import de.kaleidox.crystalshard.core.net.request.HttpMethod;
import de.kaleidox.crystalshard.core.net.request.WebRequest;
import de.kaleidox.crystalshard.core.net.request.endpoint.DiscordEndpoint;
import de.kaleidox.crystalshard.internal.items.permission.PermissionOverrideInternal;
import de.kaleidox.crystalshard.internal.items.server.interactive.InviteInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.exception.DiscordPermissionException;
import de.kaleidox.crystalshard.main.handling.editevent.EditTrait;
import de.kaleidox.crystalshard.main.items.channel.Channel;
import de.kaleidox.crystalshard.main.items.channel.ChannelCategory;
import de.kaleidox.crystalshard.main.items.channel.ServerChannel;
import de.kaleidox.crystalshard.main.items.channel.ServerVoiceChannel;
import de.kaleidox.crystalshard.main.items.permission.Permission;
import de.kaleidox.crystalshard.main.items.permission.PermissionOverride;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.server.interactive.MetaInvite;
import de.kaleidox.crystalshard.main.items.user.User;
import de.kaleidox.util.helpers.FutureHelper;
import de.kaleidox.util.helpers.ListHelper;
import de.kaleidox.util.helpers.OptionalHelper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import static de.kaleidox.crystalshard.main.handling.editevent.enums.ChannelEditTrait.*;

public class ServerVoiceChannelInternal extends VoiceChannelInternal implements ServerVoiceChannel {
    final static ConcurrentHashMap<Long, ServerVoiceChannel> instances = new ConcurrentHashMap<>();
    final List<PermissionOverride> overrides;
    final Server server;
    private int position;
    String name;
    ChannelCategory category;

    public ServerVoiceChannelInternal(Discord discord, Server server, JsonNode data) {
        super(discord, data);
        this.server = server;
        updateData(data);

        this.overrides = new ArrayList<>();
        data.path("permission_overwrites")
                .forEach(node -> overrides.add(new PermissionOverrideInternal(discord, server, node)));

        instances.put(id, this);
    }

    // Override Methods
    @Override
    public Set<EditTrait<Channel>> updateData(JsonNode data) {
        Set<EditTrait<Channel>> traits = new HashSet<>();

        if (bitrate != data.path("bitrate")
                .asInt(bitrate)) {
            bitrate = data.get("bitrate")
                    .asInt();
            traits.add(BITRATE);
        }
        if (limit != data.path("limit")
                .asInt(limit)) {
            limit = data.get("limit")
                    .asInt();
            traits.add(USER_LIMIT);
        }
        if (this.category == null && data.has("parent_id")) {
            long parentId = data.path("parent_id")
                    .asLong();
            this.category = parentId == -1 ? null : discord.getChannelCache()
                    .getOrRequest(parentId, parentId)
                    .toChannelCategory()
                    .orElse(null);
        } else if (this.category != null && !data.has("parent_id")) {
            this.category = null;
        }
        if (!name.equals(data.path("name")
                .asText(name))) {
            name = data.get("name")
                    .asText();
            traits.add(NAME);
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
    public CompletableFuture<Collection<MetaInvite>> getChannelInvites() {
        if (!hasPermission(discord, Permission.MANAGE_CHANNELS))
            return FutureHelper.failedFuture(new DiscordPermissionException(
                    "Cannot get channel invite!",
                    Permission.MANAGE_CHANNELS));
        WebRequest<Collection<MetaInvite>> request = CoreDelegate.webRequest(discord);
        return request.setMethod(HttpMethod.GET)
                .setUri(DiscordEndpoint.CHANNEL_INVITE.createUri(id))
                .executeAs(data -> {
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
    public String getName() {
        return name;
    }

    @Override
    public int getPosition() {
        return position;
    }

    @Override
    public boolean hasPermission(User user, Permission permission) {
        return OptionalHelper.or(overrides.stream()
                .filter(override -> override.getParent() != null)
                .filter(override -> override.getParent()
                        .equals(user))
                .map(override -> override.getAllowed()
                        .contains(permission))
                .findAny(), () -> this.getCategory()
                .flatMap(channelCategory -> channelCategory.getPermissionOverrides()
                        .stream()
                        .filter(override -> override.getParent() != null)
                        .filter(override -> override.getParent()
                                .equals(user))
                        .findAny())
                .map(override -> override.getAllowed()
                        .contains(permission)))
                .orElseGet(() -> toServerChannel().map(ServerChannel::getServer)
                        .orElseThrow(AssertionError::new)
                        .getEveryoneRole()
                        .getPermissions()
                        .contains(Permission.SEND_MESSAGES));
    }

    @Override
    public ServerVoiceChannel.Updater getUpdater() {
        return new ChannelUpdaterInternal.ServerVoiceChannelUpdater(discord, this);
    }
}
