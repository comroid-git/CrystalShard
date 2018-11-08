package de.kaleidox.crystalshard.internal.handling.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.user.UserUpdateEventInternal;
import de.kaleidox.crystalshard.internal.items.user.UserInternal;
import de.kaleidox.crystalshard.main.handling.editevent.EditTrait;
import de.kaleidox.crystalshard.main.handling.listener.user.UserUpdateListener;
import de.kaleidox.crystalshard.main.items.user.User;
import java.util.Set;

public class USER_UPDATE extends HandlerBase {
    // Override Methods
    @Override
    public void handle(DiscordInternal discord, JsonNode data) {
        UserInternal user = (UserInternal) discord.getUserCache()
                .getOrCreate(discord, data);

        Set<EditTrait<User>> traits = user.updateData(data);
        UserUpdateEventInternal event = new UserUpdateEventInternal(discord, user, traits);

        collectListeners(UserUpdateListener.class, discord, user).forEach(listener -> discord.getThreadPool()
                .execute(() -> listener.onUserUpdate(event)));
    }
}
