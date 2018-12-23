package de.kaleidox.crystalshard.internal.items.server;

import de.kaleidox.crystalshard.core.CoreInjector;
import de.kaleidox.crystalshard.core.net.request.HttpMethod;
import de.kaleidox.crystalshard.core.net.request.endpoint.DiscordEndpoint;
import de.kaleidox.crystalshard.internal.items.channel.ChannelBuilderInternal;
import de.kaleidox.crystalshard.internal.items.role.RoleBuilderInternal;
import de.kaleidox.crystalshard.internal.util.Container;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.items.channel.ServerChannel;
import de.kaleidox.crystalshard.main.items.role.Role;
import de.kaleidox.crystalshard.main.items.server.DefaultMessageNotificationLevel;
import de.kaleidox.crystalshard.main.items.server.ExplicitContentFilterLevel;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.server.ServerComponent;
import de.kaleidox.crystalshard.main.items.server.VerificationLevel;
import de.kaleidox.crystalshard.main.items.server.VoiceRegion;
import de.kaleidox.util.FileType;

import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class ServerBuilderInternal implements Server.Builder {
    private final Discord discord;
    private String name;
    private VoiceRegion region;
    private File icon;
    private VerificationLevel verificationLevel;
    private DefaultMessageNotificationLevel notificationLevel;
    private ExplicitContentFilterLevel contentFilter;
    private List<RoleBuilderInternal> roleBuilders;
    private List<ServerChannel.Builder> channelBuilders;

    public ServerBuilderInternal(Discord discord) {
        this.discord = discord;
        this.roleBuilders = new ArrayList<>();
        this.channelBuilders = new ArrayList<>();
    }

    // Override Methods
    @Override
    public Server.Builder setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public Server.Builder setRegion(VoiceRegion region) {
        this.region = region;
        return this;
    }

    @Override
    public Server.Builder setIcon(File icon) {
        this.icon = icon;
        return this;
    }

    @Override
    public Server.Builder setVerificationLevel(VerificationLevel level) {
        this.verificationLevel = level;
        return this;
    }

    @Override
    public Server.Builder setDefaultNotificationLevel(DefaultMessageNotificationLevel level) {
        this.notificationLevel = level;
        return this;
    }

    @Override
    public Server.Builder setExplicitContentFilter(ExplicitContentFilterLevel level) {
        this.contentFilter = level;
        return this;
    }

    @Override
    public Server.Builder addRole(Role.Builder roleBuilder) {
        roleBuilders.add((RoleBuilderInternal) roleBuilder);
        return this;
    }

    @Override
    public Server.Builder addChannel(ServerChannel.Builder channelBuilder) {
        channelBuilders.add(channelBuilder);
        return this;
    }

    @Override
    public Server.Builder add(ServerComponent component) {
        if (component instanceof Role.Builder) return addRole((Role.Builder) component);
        else if (component instanceof ServerChannel.Builder) return addChannel((ServerChannel.Builder) component);
        else throw new AssertionError("Unknown component: " + component); // only components can be added to this
    }

    @Override
    public CompletableFuture<Server> build() {
        return CoreInjector.webRequest(Server.class, discord)
                .setMethod(HttpMethod.POST)
                .setUri(DiscordEndpoint.GUILD.createUri())
                .setNode("name",
                        name,
                        "region",
                        region.getRegionKey(),
                        "icon",
                        Container.encodeBase64(icon,
                                FileType.IMAGE.JPEG,
                                new Dimension(128,
                                        128)),
                        "verification_level",
                        verificationLevel.getId(),
                        "default_message_notifications",
                        notificationLevel.getId(),
                        "explicit_content_filter",
                        contentFilter.getId(),
                        "roles",
                        roleBuilders.stream()
                                .map(RoleBuilderInternal::toJsonNode)
                                .collect(Collectors.toList()),
                        "channels",
                        channelBuilders.stream()
                                .map(builder -> ((ChannelBuilderInternal.ServerChannelBuilder) builder).toPartialJsonNode())
                                .collect(Collectors.toList()))
                .executeAs(node -> discord.getServerCache()
                        .getOrCreate(discord, node));
    }
}
