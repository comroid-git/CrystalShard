package de.kaleidox.crystalshard.main.items.server.emoji;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.InternalDelegate;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.items.Mentionable;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.util.Castable;
import de.kaleidox.crystalshard.util.annotations.NotNull;
import de.kaleidox.crystalshard.util.annotations.Nullable;

import java.util.Optional;

public interface Emoji extends Mentionable, Castable<Emoji> {
    /**
     * Gets the alias or name of this emoji.
     *
     * @return The alias of the emoji.
     */
    String toAlias();
    
    /**
     * Returns a String for a {@link Message} that will get replaced by Discord with a visual of this emoji. This method is analogous to {@link
     * Mentionable#getMentionTag()}.
     *
     * @return A discord-printable string of this emoji.
     */
    String toDiscordPrintable();
    
    /**
     * Gets the universal replacement of this emoji.
     *
     * @return universal replacement of the emoji.
     */
    @Override
    String toString();
    
    @Override
    boolean equals(Object obj);
    
    /**
     * Gets the name of this emoji.
     *
     * @return The name of the emoji.
     */
    default String getName() {
        return this.toAlias();
    }
    
    default Optional<UnicodeEmoji> toUnicodeEmoji() {
        return castTo(UnicodeEmoji.class);
    }
    
    default Optional<CustomEmoji> toCustomEmoji() {
        return castTo(CustomEmoji.class);
    }
    
    static Emoji of(@NotNull Discord discord, @Nullable Server server, @NotNull JsonNode data) {
        if (data.get("id").isNull()) {
            return InternalDelegate.newInstance(UnicodeEmoji.class, discord, data, true);
        } else {
            return discord.getEmojiCache().getOrCreate(discord, server, data, true);
        }
    }
}
