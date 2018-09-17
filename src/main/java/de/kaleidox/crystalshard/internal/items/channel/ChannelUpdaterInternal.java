package de.kaleidox.crystalshard.internal.items.channel;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.kaleidox.crystalshard.core.net.request.Endpoint;
import de.kaleidox.crystalshard.core.net.request.Method;
import de.kaleidox.crystalshard.core.net.request.WebRequest;
import de.kaleidox.crystalshard.internal.items.permission.PermissionOverrideInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.handling.editevent.EditTrait;
import de.kaleidox.crystalshard.main.items.channel.Channel;
import de.kaleidox.crystalshard.main.items.channel.ChannelCategory;
import de.kaleidox.crystalshard.main.items.channel.ServerChannel;
import de.kaleidox.crystalshard.main.items.channel.TextChannel;
import de.kaleidox.crystalshard.main.items.channel.VoiceChannel;
import de.kaleidox.crystalshard.main.items.permission.PermissionOverride;
import de.kaleidox.util.helpers.JsonHelper;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static de.kaleidox.crystalshard.main.handling.editevent.enums.ChannelEditTrait.*;
import static de.kaleidox.util.helpers.JsonHelper.*;

public class ChannelUpdaterInternal {
    public static class TextChannelUpdater implements TextChannel.Updater {
        private final boolean                  isPrivate;
        private final Discord                  discord;
        private final long                     id;
        private final Set<EditTrait<Channel>>  edited;
        private       String                   name;
        private       String                   topic;
        private       int                      position;
        private       boolean                  nsfw;
        private       ChannelCategory          parent;
        private       List<PermissionOverride> overrides;
        
        TextChannelUpdater(TextChannel channel) {
            this.discord = channel.getDiscord();
            this.isPrivate = channel.isPrivate();
            this.id = channel.getId();
            this.name = channel.getName();
            this.topic = channel.getTopic();
            this.position = channel.getPosition();
            this.nsfw = channel.isNsfw();
            this.parent = channel.toServerChannel().flatMap(ServerChannel::getCategory).orElse(null);
            this.overrides = channel.toServerChannel()
                    .map(ServerChannel::getPermissionOverrides)
                    .orElse(Collections.emptyList());
            this.edited = new HashSet<>();
        }
        
// Override Methods
        @Override
        public TextChannel.Updater setTopic(String topic) {
            this.topic = topic;
            edited.add(TOPIC);
            return this;
        }
        
        @Override
        public TextChannel.Updater setNsfw(boolean nsfw) {
            this.nsfw = nsfw;
            edited.add(NSFW_FLAG);
            return this;
        }
        
        @Override
        public TextChannel.Updater setParent(ChannelCategory parent) {
            if (isPrivate) throw new IllegalStateException("Channel is not a Server Channel!");
            this.parent = parent;
            edited.add(PARENT_ID);
            return this;
        }
        
        @Override
        public Channel.Updater setName(String name) {
            this.name = name;
            edited.add(NAME);
            return this;
        }
        
        @Override
        public Channel.Updater setPosition(int position) {
            this.position = position;
            edited.add(POSITION);
            return this;
        }
        
        @Override
        public Channel.Updater modifyOverrides(Consumer<List<PermissionOverride>> overrideModifier) {
            if (isPrivate) throw new IllegalStateException("Channel is not a Server Channel!");
            overrideModifier.accept(overrides);
            edited.add(PERMISSION_OVERWRITES);
            return this;
        }
        
        @Override
        public CompletableFuture<Void> update() {
            ObjectNode node = JsonHelper.objectNode();
            if (edited.contains(NAME)) node.set("name", nodeOf(name));
            if (edited.contains(POSITION)) node.set("position", nodeOf(position));
            if (edited.contains(TOPIC)) node.set("topic", nodeOf(topic));
            if (edited.contains(NSFW_FLAG)) node.set("nsfw", nodeOf(nsfw));
            if (edited.contains(PARENT_ID)) node.set("parent_id", nodeOf(parent.getId()));
            if (edited.contains(PERMISSION_OVERWRITES)) {
                node.set("permission_overwrites",
                         arrayNode(overrides.stream()
                                           .map(PermissionOverrideInternal.class::cast)
                                           .map(PermissionOverrideInternal::toJsonNode)
                                           .collect(Collectors.toList())));
            }
            return new WebRequest<Void>(discord).method(Method.PATCH)
                    .endpoint(Endpoint.Location.CHANNEL.toEndpoint(id))
                    .node(node)
                    .executeNull();
        }
    }
    
    public static class VoiceChannelUpdater implements VoiceChannel.Updater {
        private final boolean                  isPrivate;
        private final Discord                  discord;
        private final long                     id;
        private final Set<EditTrait<Channel>>  edited;
        private       String                   name;
        private       int                      position;
        private       int                      bitrate;
        private       int                      limit;
        private       ChannelCategory          parent;
        private       List<PermissionOverride> overrides;
        
        VoiceChannelUpdater(VoiceChannel channel) {
            this.discord = channel.getDiscord();
            this.isPrivate = channel.isPrivate();
            this.id = channel.getId();
            this.name = channel.getName();
            this.bitrate = channel.getBitrate();
            this.position = channel.getPosition();
            this.limit = channel.getUserLimit();
            this.parent = channel.toServerChannel().flatMap(ServerChannel::getCategory).orElse(null);
            this.overrides = channel.toServerChannel()
                    .map(ServerChannel::getPermissionOverrides)
                    .orElse(Collections.emptyList());
            this.edited = new HashSet<>();
        }
        
// Override Methods
        @Override
        public VoiceChannel.Updater setBitrate(int bitrate) {
            this.bitrate = bitrate;
            edited.add(BITRATE);
            return this;
        }
        
        @Override
        public VoiceChannel.Updater setUserLimit(int limit) {
            this.limit = limit;
            edited.add(USER_LIMIT);
            return this;
        }
        
        @Override
        public VoiceChannel.Updater setParent(ChannelCategory parent) {
            if (isPrivate) throw new IllegalStateException("Channel is not a Server Channel!");
            this.parent = parent;
            edited.add(PARENT_ID);
            return this;
        }
        
        @Override
        public Channel.Updater setName(String name) {
            this.name = name;
            edited.add(NAME);
            return this;
        }
        
        @Override
        public Channel.Updater setPosition(int position) {
            this.position = position;
            edited.add(POSITION);
            return this;
        }
        
        @Override
        public Channel.Updater modifyOverrides(Consumer<List<PermissionOverride>> overrideModifier) {
            if (isPrivate) throw new IllegalStateException("Channel is not a Server Channel!");
            overrideModifier.accept(overrides);
            edited.add(PERMISSION_OVERWRITES);
            return this;
        }
        
        @Override
        public CompletableFuture<Void> update() {
            ObjectNode node = JsonHelper.objectNode();
            if (edited.contains(NAME)) node.set("name", nodeOf(name));
            if (edited.contains(POSITION)) node.set("position", nodeOf(position));
            if (edited.contains(USER_LIMIT)) node.set("user_limit", nodeOf(limit));
            if (edited.contains(BITRATE)) node.set("bitrate", nodeOf(bitrate));
            if (edited.contains(PARENT_ID)) node.set("parent_id", nodeOf(parent.getId()));
            if (edited.contains(PERMISSION_OVERWRITES)) {
                node.set("permission_overwrites",
                         arrayNode(overrides.stream()
                                           .map(PermissionOverrideInternal.class::cast)
                                           .map(PermissionOverrideInternal::toJsonNode)
                                           .collect(Collectors.toList())));
            }
            return new WebRequest<Void>(discord).method(Method.PATCH)
                    .endpoint(Endpoint.Location.CHANNEL.toEndpoint(id))
                    .node(node)
                    .executeNull();
        }
    }
}
