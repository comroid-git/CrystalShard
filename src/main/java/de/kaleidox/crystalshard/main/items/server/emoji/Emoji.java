package de.kaleidox.crystalshard.main.items.server.emoji;

import com.fasterxml.jackson.databind.JsonNode;
import com.vdurmont.emoji.EmojiParser;
import de.kaleidox.crystalshard.internal.items.server.emoji.CustomEmojiInternal;
import de.kaleidox.crystalshard.internal.items.server.emoji.UnicodeEmojiInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.items.Mentionable;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.util.Castable;
import de.kaleidox.util.annotations.NotNull;
import de.kaleidox.util.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface Emoji extends Mentionable, Castable<Emoji> {
    static CompletableFuture<Emoji> of(@NotNull Discord discord,
                                       @Nullable Server server,
                                       @NotNull String anyEmoji) {
        Objects.requireNonNull(anyEmoji);
        String aliases = EmojiParser.parseToAliases(anyEmoji);
        String unicode = EmojiParser.parseToUnicode(anyEmoji);
        if (aliases.equalsIgnoreCase(anyEmoji) && unicode.equalsIgnoreCase(anyEmoji)) {
            // is likely customEmoji
            return CustomEmojiInternal.getInstance(server, anyEmoji).thenApply(Emoji.class::cast);
        } else {
            // is likely unicodeEmoji
            return CompletableFuture.completedFuture(new UnicodeEmojiInternal(discord, aliases, unicode));
        }
    }

    static Emoji of(@NotNull Discord discord,
                    @Nullable Server server,
                    @NotNull JsonNode data) {
        if (data.get("id").isNull()) {
            return new UnicodeEmojiInternal(discord, data, true);
        } else {
            return CustomEmojiInternal.getInstance(discord, server, data, true);
        }
    }

    /**
     * Gets the alias or name of this emoji.
     *
     * @return The alias of the emoji.
     */
    String toAlias();

    /**
     * Gets the name of this emoji.
     *
     * @return The name of the emoji.
     */
    default String getName() {
        return this.toAlias();
    }

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
     * Returns a String for a {@link Message} that will get replaced by Discord with a visual of this emoji.
     * This method is analogous to {@link Mentionable#getMentionTag()}.
     *
     * @return A discord-printable string of this emoji.
     */
    String toDiscordPrintable();

    default Optional<UnicodeEmoji> toUnicodeEmoji() {
        return castTo(UnicodeEmoji.class);
    }

    default Optional<CustomEmoji> toCustomEmoji() {
        return castTo(CustomEmoji.class);
    }
}
