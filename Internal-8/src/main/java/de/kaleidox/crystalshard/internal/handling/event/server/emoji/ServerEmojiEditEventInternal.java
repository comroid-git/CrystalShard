package de.kaleidox.crystalshard.internal.handling.event.server.emoji;

import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.EventBase;
import de.kaleidox.crystalshard.main.handling.editevent.EditTrait;
import de.kaleidox.crystalshard.main.handling.event.server.other.ServerEmojiEditEvent;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.server.emoji.CustomEmoji;
import java.util.List;
import java.util.Set;

public class ServerEmojiEditEventInternal extends EventBase implements ServerEmojiEditEvent {
    private final Server server;
    private final List<CustomEmoji> added;
    private final List<CustomEmoji> edited;
    private final List<CustomEmoji> deleted;
    private final Set<EditTrait<CustomEmoji>> traits;

    public ServerEmojiEditEventInternal(DiscordInternal discordInternal, Server server, List<CustomEmoji> added, List<CustomEmoji> edited,
                                        List<CustomEmoji> deleted, Set<EditTrait<CustomEmoji>> traits) {
        super(discordInternal);
        this.server = server;
        this.added = added;
        this.edited = edited;
        this.deleted = deleted;
        this.traits = traits;
    }

    // Override Methods
    @Override
    public Set<EditTrait<CustomEmoji>> getEditTraits() {
        return traits;
    }

    @Override
    public Server getServer() {
        return server;
    }

    @Override
    public List<CustomEmoji> getAddedEmojis() {
        return added;
    }

    @Override
    public List<CustomEmoji> getEditedEmojis() {
        return edited;
    }

    @Override
    public List<CustomEmoji> getDeletedEmojis() {
        return deleted;
    }
}
