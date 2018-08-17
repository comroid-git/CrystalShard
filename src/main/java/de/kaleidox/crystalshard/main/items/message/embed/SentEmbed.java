package de.kaleidox.crystalshard.main.items.message.embed;

import java.util.Optional;

public interface SentEmbed extends Embed {
    Optional<EmbedDraft> toEmbedDraft();
}
