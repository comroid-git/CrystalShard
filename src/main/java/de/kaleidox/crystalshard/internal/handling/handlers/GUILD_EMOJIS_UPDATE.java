package de.kaleidox.crystalshard.internal.handling.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.server.emoji.ServerEmojiEditEventInternal;
import de.kaleidox.crystalshard.internal.items.server.ServerInternal;
import de.kaleidox.crystalshard.internal.items.server.emoji.CustomEmojiInternal;
import de.kaleidox.crystalshard.main.handling.editevent.EditTrait;
import de.kaleidox.crystalshard.main.handling.listener.server.ServerEmojiEditListener;
import de.kaleidox.crystalshard.main.items.server.emoji.CustomEmoji;
import de.kaleidox.util.helpers.ListHelper;
import de.kaleidox.util.objects.Difference;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GUILD_EMOJIS_UPDATE extends HandlerBase {
    @Override
    public void handle(DiscordInternal discord, JsonNode data) {
        ServerInternal server = (ServerInternal) ServerInternal.getInstance(discord, data.get("guild_id").asLong());
        List<CustomEmoji> newEmojis = new ArrayList<>();
        Set<EditTrait<CustomEmoji>> traits = new HashSet<>();
        List<CustomEmoji> edited = new ArrayList<>();
        Difference<CustomEmoji> diff;

        data.get("emojis").forEach(node -> {
            CustomEmojiInternal emoji = (CustomEmojiInternal) CustomEmojiInternal.getInstance(
                    discord, server, data, true);
            Set<EditTrait<CustomEmoji>> editTraits = emoji.updateData(node);
            if (!editTraits.isEmpty()) edited.add(emoji);
            traits.addAll(editTraits);
            newEmojis.add(emoji);
        });
        diff = ListHelper.getDifference((List<CustomEmoji>) server.getCustomEmojis(), newEmojis);

        ServerEmojiEditEventInternal event = new ServerEmojiEditEventInternal(
                discord, server, diff.getAdded(), edited, diff.getRemoved(), traits);

        collectListeners(ServerEmojiEditListener.class, discord, server)
                .forEach(listener -> discord.getThreadPool()
                        .execute(() -> listener.onEmojiEdit(event))
                );
    }
}
