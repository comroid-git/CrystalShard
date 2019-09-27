package de.kaleidox.crystalshard.core.gateway.event.user.presence;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import de.kaleidox.crystalshard.abstraction.gateway.AbstractGatewayEvent;
import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.entity.EntityType;
import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.api.entity.guild.Role;
import de.kaleidox.crystalshard.api.entity.user.User;
import de.kaleidox.crystalshard.api.model.user.Presence;
import de.kaleidox.crystalshard.core.annotation.JsonData;
import de.kaleidox.crystalshard.core.api.gateway.event.user.presence.PresenceUpdateEvent;
import de.kaleidox.crystalshard.util.annotation.IntroducedBy;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.Nullable;

import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;

@IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/topics/gateway#presence-update")
public class PresenceUpdateEventImpl extends AbstractGatewayEvent implements PresenceUpdateEvent {
    protected @JsonData("user") User user;
    protected @JsonData(value = "roles", type = Long.class) Collection<Long> roleIds;
    protected @JsonData(value = "game") @Nullable Presence.Activity activity;
    protected @JsonData("guild_id") long guildId;
    protected @JsonData("status") Presence.Status status;
    protected @JsonData(value = "activities", type = Presence.Activity.class) Collection<Presence.Activity> activities;
    protected @JsonData("client_status") Presence.ClientStatus clientStatus;

    private Guild guild;
    private Collection<Role> roles;

    public PresenceUpdateEventImpl(Discord api, JsonNode data) {
        super(api, data);

        guild = api.getCacheManager()
                .getGuildByID(guildId)
                .orElseThrow(() -> new AssertionError("No valid Guild ID was sent with this PresenceUpdateEvent!"));
        roles = roleIds.stream()
                .flatMap(id -> api.getCacheManager()
                        .getSnowflakesByID(id)
                        .stream())
                .filter(flake -> flake.getEntityType() == EntityType.ROLE)
                .map(Role.class::cast)
                .collect(Collectors.toUnmodifiableList());

        affects(guild);
        affects(user);
        user.asGuildMember(guild).ifPresent(member -> {
            affects(member);

            member.getRoles().forEach(this::affects);
        });
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public Collection<Role> getRoles() {
        return roles;
    }

    @Override
    public Optional<Presence.Activity> getActivity() {
        return Optional.ofNullable(activity);
    }

    @Override
    public Guild getGuild() {
        return guild;
    }

    @Override
    public Presence.Status getStatus() {
        return status;
    }

    @Override
    public Collection<Presence.Activity> getActivities() {
        return activities;
    }

    @Override
    public Presence.ClientStatus getClientStatus() {
        return clientStatus;
    }
}
