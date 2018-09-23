package de.kaleidox.crystalshard.internal.handling.event.server.member;

import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.EventBase;
import de.kaleidox.crystalshard.main.handling.editevent.EditTrait;
import de.kaleidox.crystalshard.main.handling.event.server.member.ServerMemberUpdateEvent;
import de.kaleidox.crystalshard.main.items.role.Role;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.user.ServerMember;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ServerMemberUpdateEventInternal extends EventBase implements ServerMemberUpdateEvent {
    private final List<Role>                   roles;
    private final String                       nickname;
    private final Set<EditTrait<ServerMember>> traits;
    private final ServerMember                 member;
    private final Server                       server;
    
    public ServerMemberUpdateEventInternal(DiscordInternal discordInternal, List<Role> roles, String nickname,
                                           Set<EditTrait<ServerMember>> traits, ServerMember member, Server server) {
        super(discordInternal);
        this.roles = roles;
        this.nickname = nickname;
        this.traits = traits;
        this.member = member;
        this.server = server;
    }
    
    // Override Methods
    @Override
    public List<Role> getMemberRoles() {
        return roles;
    }
    
    @Override
    public Optional<String> getNickname() {
        return Optional.ofNullable(nickname.equals(member.getName()) ? null : nickname);
    }
    
    @Override
    public Set<EditTrait<ServerMember>> getEditTraits() {
        return traits;
    }
    
    @Override
    public ServerMember getMember() {
        return member;
    }
    
    @Override
    public Server getServer() {
        return server;
    }
}
