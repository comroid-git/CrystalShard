package de.comroid.crystalshard.impl.entity.channel;

import java.util.Collection;

import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.api.entity.channel.PrivateChannel;
import de.comroid.crystalshard.api.entity.user.User;
import de.comroid.crystalshard.core.annotation.JsonData;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractPrivateChannel<Self extends AbstractPrivateChannel<Self>>
        extends AbstractChannel<Self>
        implements PrivateChannel {
    protected final User owner;
    protected @JsonData(value = "recipients", type = User.class) Collection<User> recipients;
    protected @JsonData("icon") @Nullable String iconHash;
    protected @JsonData("owner_id") long ownerId;
    protected @JsonData("application_id") long applicationId;

    protected AbstractPrivateChannel(Discord api, JsonNode data) {
        super(api, data);

        owner = api.getCacheManager()
                .getUserByID(ownerId == -1 ? applicationId : ownerId)
                .orElseThrow(() -> new AssertionError("No valid Owner ID was sent with this PrivateChannel!"));
    }
}
