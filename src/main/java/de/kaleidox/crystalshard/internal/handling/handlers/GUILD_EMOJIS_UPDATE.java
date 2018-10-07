package de.kaleidox.crystalshard.internal.handling.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.server.emoji.ServerEmojiEditEventInternal;
import de.kaleidox.crystalshard.internal.items.server.ServerInternal;
import de.kaleidox.crystalshard.internal.items.server.emoji.CustomEmojiInternal;
import de.kaleidox.crystalshard.main.handling.editevent.EditTrait;
import de.kaleidox.crystalshard.main.handling.listener.server.ServerEmojiEditListener;
import de.kaleidox.crystalshard.main.items.server.emoji.CustomEmoji;
import de.kaleidox.crystalshard.util.helpers.ListHelper;
import de.kaleidox.crystalshard.util.objects.Difference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GUILD_EMOJIS_UPDATE extends HandlerBase {
    // Override Methods
    @Override
    public void handle(DiscordInternal discord, JsonNode data) {
        long serverId = data.get("guild_id")
                .asLong();
        ServerInternal server = (ServerInternal) discord.getServerCache()
                .getOrRequest(serverId, serverId);
        List<CustomEmoji> newEmojis = new ArrayList<>();
        Set<EditTrait<CustomEmoji>> traits = new HashSet<>();
        List<CustomEmoji> edited = new ArrayList<>();
        Difference<CustomEmoji> diff;
        
        for (JsonNode node : data.get("emojis")) {
            CustomEmojiInternal emoji = (CustomEmojiInternal) discord.getEmojiCache()
                    .getOrCreate(discord, server, data, true);
            Set<EditTrait<CustomEmoji>> editTraits = emoji.updateData(node);
            if (!editTraits.isEmpty()) edited.add(emoji);
            traits.addAll(editTraits);
            newEmojis.add(emoji);
        }
        diff = ListHelper.getDifference((List<CustomEmoji>) server.getCustomEmojis(), newEmojis);
        server.replaceEmojis(newEmojis);
        
        ServerEmojiEditEventInternal event = new ServerEmojiEditEventInternal(discord, server, diff.getAdded(), edited, diff.getRemoved(), traits);
        
        collectListeners(ServerEmojiEditListener.class, discord, server).forEach(listener -> discord.getThreadPool()
                .execute(() -> listener.onEmojiEdit(event)));
    }
}
