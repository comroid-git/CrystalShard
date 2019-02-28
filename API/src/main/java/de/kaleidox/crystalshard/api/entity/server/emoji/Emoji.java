package de.kaleidox.crystalshard.api.entity.server.emoji;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.entity.Mentionable;
import de.kaleidox.crystalshard.api.entity.message.Message;
import de.kaleidox.crystalshard.api.entity.server.Server;
import de.kaleidox.crystalshard.api.util.Castable;

import java.util.Optional;

public interface Emoji extends Mentionable, Castable<Emoji> {
    /**
     * Returns a String for a {@link Message} that will get replaced by Discord with a visual of this emoji. This method is analogous to {@link
     * Mentionable#getMentionTag()}.
     *
     * @return A discord-printable string of this emoji.
     */
    String toDiscordPrintable();

    @Override
    boolean equals(Object obj);

    /**
     * Gets the universal replacement of this emoji.
     *
     * @return universal replacement of the emoji.
     */
    @Override
    String toString();

    /**
     * Gets the name of this emoji.
     *
     * @return The name of the emoji.
     */
    default String getName() {
        return this.toAlias();
    }

    /**
     * Gets the alias or name of this emoji.
     *
     * @return The alias of the emoji.
     */
    String toAlias();

    default Optional<UnicodeEmoji> toUnicodeEmoji() {
        return castTo(UnicodeEmoji.class);
    }

    default Optional<CustomEmoji> toCustomEmoji() {
        return castTo(CustomEmoji.class);
    }

    static Emoji of(@NotNull Discord discord, @Nullable Server server, @NotNull JsonNode data) {
        if (data.get("id")
                .isNull()) {
            return Injector.create(UnicodeEmoji.class, discord, data, true);
        } else {
            return discord.getEmojiCache()
                    .getOrCreate(discord, server, data, true);
        }
    }
}
